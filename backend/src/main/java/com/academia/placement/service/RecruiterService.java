package com.academia.placement.service;

import com.academia.placement.dto.CreateRequisitionRequest;
import com.academia.placement.dto.JobRequisitionDto;
import com.academia.placement.dto.RequisitionSkillDto;
import com.academia.placement.model.*;
import com.academia.placement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RecruiterService {

    private final JobRequisitionRepository jobRequisitionRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final SkillRepository skillRepository;
    private final ApplicationRepository applicationRepository;
    private final AuditService auditService;

    public RecruiterService(JobRequisitionRepository jobRequisitionRepository,
                            RecruiterProfileRepository recruiterProfileRepository,
                            SkillRepository skillRepository,
                            ApplicationRepository applicationRepository,
                            AuditService auditService) {
        this.jobRequisitionRepository = jobRequisitionRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.skillRepository = skillRepository;
        this.applicationRepository = applicationRepository;
        this.auditService = auditService;
    }

    @Transactional
    public JobRequisitionDto createRequisition(UUID userId, CreateRequisitionRequest request) {
        RecruiterProfile recruiter = recruiterProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Recruiter profile not found for user: " + userId));

        JobRequisition requisition = new JobRequisition(
                recruiter.getCompany(),
                recruiter,
                request.getTitle(),
                request.getDescription(),
                request.getMinCgpa(),
                request.getJobType(),
                request.getCtcLpa(),
                request.getDeadline()
        );

        List<RequisitionSkill> reqSkills = new ArrayList<>();
        for (CreateRequisitionRequest.RequisitionSkillInput si : request.getSkills()) {
            Skill skill = skillRepository.findById(si.getSkillId())
                    .orElseThrow(() -> new IllegalArgumentException("Skill not found: " + si.getSkillId()));

            reqSkills.add(new RequisitionSkill(requisition, skill, si.getWeight(), si.getMinProficiency()));
        }
        requisition.setRequiredSkills(reqSkills);

        requisition = jobRequisitionRepository.save(requisition);

        auditService.recordEvent(
                userId,
                "RECRUITER",
                "CREATE_JOB_REQUISITION",
                "JobRequisition",
                requisition.getId(),
                "Created requisition: " + requisition.getTitle() + " with " + reqSkills.size() + " required skills",
                "0.0.0.0"
        );

        return toDto(requisition);
    }

    @Transactional(readOnly = true)
    public List<JobRequisitionDto> getAllOpenRequisitions() {
        return jobRequisitionRepository.findByStatus("OPEN").stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobRequisitionDto getRequisitionById(UUID id) {
        JobRequisition req = jobRequisitionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job requisition not found: " + id));
        return toDto(req);
    }

    @Transactional
    public void updateApplicationStage(UUID applicationId, ApplicationStage stage, UUID recruiterUserId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found for id: " + applicationId));

        ApplicationStage previousStage = application.getCurrentStage();
        application.setCurrentStage(stage);
        applicationRepository.save(application);

        if (stage == ApplicationStage.OFFERED) {
            StudentProfile student = application.getStudent();
            student.setPlaced(true);
        }

        auditService.recordEvent(
                recruiterUserId,
                "RECRUITER",
                "UPDATE_APPLICATION_STAGE",
                "Application",
                application.getId(),
                "Shifted application stage from " + previousStage + " to " + stage,
                "0.0.0.0"
        );
    }

    public JobRequisitionDto toDto(JobRequisition r) {
        List<RequisitionSkillDto> skillDtos = r.getRequiredSkills() != null
                ? r.getRequiredSkills().stream()
                .map(rs -> new RequisitionSkillDto(
                        rs.getSkill().getId(),
                        rs.getSkill().getCanonicalName(),
                        rs.getWeight(),
                        rs.getMinProficiency()
                ))
                .collect(Collectors.toList())
                : new ArrayList<>();

        return new JobRequisitionDto(
                r.getId(),
                r.getCompany() != null ? r.getCompany().getName() : "Enterprise Partner",
                r.getTitle(),
                r.getDescription(),
                r.getMinCgpa(),
                r.getJobType(),
                r.getStatus(),
                r.getCtcLpa(),
                r.getDeadline(),
                skillDtos
        );
    }
}

