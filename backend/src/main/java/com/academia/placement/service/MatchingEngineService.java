package com.academia.placement.service;

import com.academia.placement.dto.CompatibilityReportDto;
import com.academia.placement.model.*;
import com.academia.placement.repository.JobRequisitionRepository;
import com.academia.placement.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingEngineService {

    private final StudentProfileRepository studentProfileRepository;
    private final JobRequisitionRepository jobRequisitionRepository;

    @Value("${app.matching.jaccard-weight:0.40}")
    private double jaccardWeight;

    @Value("${app.matching.cosine-weight:0.60}")
    private double cosineWeight;

    @Value("${app.matching.eligibility-threshold:0.50}")
    private double eligibilityThreshold;

    public MatchingEngineService(StudentProfileRepository studentProfileRepository,
                                 JobRequisitionRepository jobRequisitionRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.jobRequisitionRepository = jobRequisitionRepository;
    }

    @Transactional(readOnly = true)
    public CompatibilityReportDto calculateCompatibility(UUID studentId, UUID requisitionId) {
        StudentProfile student = studentProfileRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found for id: " + studentId));

        JobRequisition requisition = jobRequisitionRepository.findById(requisitionId)
                .orElseThrow(() -> new IllegalArgumentException("Job requisition not found for id: " + requisitionId));

        return computeScore(student, requisition);
    }

    public CompatibilityReportDto computeScore(StudentProfile student, JobRequisition requisition) {
        List<RequisitionSkill> reqSkills = requisition.getRequiredSkills();
        List<StudentSkill> studentSkills = student.getSkills();

        boolean cgpaSatisfied = student.getCgpa() != null &&
                requisition.getMinCgpa() != null &&
                student.getCgpa().compareTo(requisition.getMinCgpa()) >= 0;

        if (reqSkills == null || reqSkills.isEmpty()) {
            return new CompatibilityReportDto(
                    student.getId(),
                    requisition.getId(),
                    requisition.getTitle(),
                    BigDecimal.ONE.setScale(4, RoundingMode.HALF_UP),
                    BigDecimal.ONE.setScale(4, RoundingMode.HALF_UP),
                    BigDecimal.ONE.setScale(4, RoundingMode.HALF_UP),
                    0, 0, cgpaSatisfied
            );
        }

        Map<UUID, BigDecimal> studentSkillMap = studentSkills.stream()
                .collect(Collectors.toMap(
                        s -> s.getSkill().getId(),
                        StudentSkill::getProficiencyLevel,
                        (existing, replacement) -> existing
                ));

        Set<UUID> reqSkillIds = reqSkills.stream()
                .map(r -> r.getSkill().getId())
                .collect(Collectors.toSet());

        Set<UUID> studentSkillIds = studentSkillMap.keySet();

        // Jaccard Set Overlap: |Intersection| / |Union|
        Set<UUID> intersection = new HashSet<>(reqSkillIds);
        intersection.retainAll(studentSkillIds);

        Set<UUID> union = new HashSet<>(reqSkillIds);
        union.addAll(studentSkillIds);

        double jaccard = union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();

        // Weighted Cosine Similarity: sum(V_cand * V_req) / (||V_cand|| * ||V_req||)
        double dotProduct = 0.0;
        double normCandSq = 0.0;
        double normReqSq = 0.0;

        for (RequisitionSkill rs : reqSkills) {
            UUID skillId = rs.getSkill().getId();
            double weight = rs.getWeight() != null ? rs.getWeight().doubleValue() : 1.0;
            double reqTargetProficiency = rs.getMinProficiency() != null ? rs.getMinProficiency().doubleValue() : 0.50;
            double vReq = weight * reqTargetProficiency;

            double candProficiency = studentSkillMap.getOrDefault(skillId, BigDecimal.ZERO).doubleValue();
            double vCand = weight * candProficiency;

            dotProduct += (vCand * vReq);
            normCandSq += (vCand * vCand);
            normReqSq += (vReq * vReq);
        }

        double normCand = Math.sqrt(normCandSq);
        double normReq = Math.sqrt(normReqSq);
        double cosine = (normCand > 0 && normReq > 0) ? (dotProduct / (normCand * normReq)) : 0.0;

        // Composite Score: alpha * Jaccard + (1 - alpha) * Cosine
        double composite = (jaccardWeight * jaccard) + (cosineWeight * cosine);

        boolean isEligible = cgpaSatisfied && (composite >= eligibilityThreshold);

        return new CompatibilityReportDto(
                student.getId(),
                requisition.getId(),
                requisition.getTitle(),
                BigDecimal.valueOf(jaccard).setScale(4, RoundingMode.HALF_UP),
                BigDecimal.valueOf(cosine).setScale(4, RoundingMode.HALF_UP),
                BigDecimal.valueOf(composite).setScale(4, RoundingMode.HALF_UP),
                intersection.size(),
                reqSkillIds.size(),
                isEligible
        );
    }
}

