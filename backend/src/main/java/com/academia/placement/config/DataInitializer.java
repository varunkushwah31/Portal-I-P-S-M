package com.academia.placement.config;

import com.academia.placement.model.*;
import com.academia.placement.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private static final String DEFAULT_SEED_PASSWORD = "Password123!";

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final CompanyRepository companyRepository;
    private final SkillCategoryRepository skillCategoryRepository;
    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final CourseCurriculumMappingRepository courseCurriculumMappingRepository;
    private final JobRequisitionRepository jobRequisitionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           StudentProfileRepository studentProfileRepository,
                           RecruiterProfileRepository recruiterProfileRepository,
                           CompanyRepository companyRepository,
                           SkillCategoryRepository skillCategoryRepository,
                           SkillRepository skillRepository,
                           StudentSkillRepository studentSkillRepository,
                           CourseCurriculumMappingRepository courseCurriculumMappingRepository,
                           JobRequisitionRepository jobRequisitionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.companyRepository = companyRepository;
        this.skillCategoryRepository = skillCategoryRepository;
        this.skillRepository = skillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.courseCurriculumMappingRepository = courseCurriculumMappingRepository;
        this.jobRequisitionRepository = jobRequisitionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (skillCategoryRepository.count() > 0) {
            return;
        }

        // 1. Skill Categories
        SkillCategory coreCs = skillCategoryRepository.save(new SkillCategory("Core Computer Science", "Fundamental algorithmic and systems theory"));
        SkillCategory storage = skillCategoryRepository.save(new SkillCategory("Data Persistence & Storage", "Relational, NoSQL, and cache storage architectures"));
        SkillCategory systems = skillCategoryRepository.save(new SkillCategory("Systems & Cloud Infrastructure", "Operating systems, containerization, and cloud primitives"));
        SkillCategory web = skillCategoryRepository.save(new SkillCategory("Enterprise Web Architecture", "Backend REST/gRPC frameworks, middleware, and security"));

        // 2. Canonical Skills
        Skill dsa = skillRepository.save(new Skill("Data Structures & Algorithms", coreCs, "Trees, graphs, dynamic programming, complexity analysis"));
        Skill ood = skillRepository.save(new Skill("Object-Oriented Design", coreCs, "SOLID principles, design patterns, domain modeling"));
        Skill os = skillRepository.save(new Skill("Operating Systems Primitives", coreCs, "Process scheduling, concurrency, virtual memory, IPC"));

        Skill postgres = skillRepository.save(new Skill("PostgreSQL & Relational Modeling", storage, "3NF normalization, ACID transactions, index optimization"));
        skillRepository.save(new Skill("Redis Caching & In-Memory Stores", storage, "Cache-aside patterns, key eviction, TTL management"));

        Skill docker = skillRepository.save(new Skill("Docker & Containerization", systems, "Multi-stage builds, container isolation, docker-compose"));
        Skill networks = skillRepository.save(new Skill("Computer Networks & Protocols", systems, "TCP/IP, HTTP/1.1-2-3, DNS, TLS termination"));

        Skill javaSpring = skillRepository.save(new Skill("Java & Spring Boot 3", web, "Spring MVC, Spring Data JPA, Spring Security, IoC"));
        skillRepository.save(new Skill("RESTful API Engineering", web, "Stateless JWT auth, idempotency, API contracts"));
        Skill reactTs = skillRepository.save(new Skill("React & TypeScript", web, "Component lifecycle, hooks, strict type safety"));

        // 3. Curriculum Mappings
        courseCurriculumMappingRepository.save(new CourseCurriculumMapping(dsa, "CS-301", "Data Structures & Algorithms", 3, "CS", "Complete lab modules on Graph Traversal, Balanced Trees, and Dynamic Programming"));
        courseCurriculumMappingRepository.save(new CourseCurriculumMapping(os, "CS-401", "Operating Systems", 4, "CS", "Review process synchronization, mutex locks, semaphores, and paging algorithms"));
        courseCurriculumMappingRepository.save(new CourseCurriculumMapping(postgres, "CS-501", "Database Management Systems", 5, "CS", "Practice normal form decompositions (BCNF/3NF), indexing B+ trees, and transaction isolation levels"));
        courseCurriculumMappingRepository.save(new CourseCurriculumMapping(networks, "CS-601", "Computer Networks", 6, "CS", "Analyze packet captures with Wireshark and implement multi-threaded socket servers"));
        courseCurriculumMappingRepository.save(new CourseCurriculumMapping(javaSpring, "CS-701", "Enterprise Web Architectures", 7, "CS", "Build multi-tier Spring Boot applications with Spring Security JWT and Hibernate JPA"));
        courseCurriculumMappingRepository.save(new CourseCurriculumMapping(docker, "CS-702", "Cloud Computing & DevOps", 7, "CS", "Hands-on containerization labs using Docker Engine and Docker Compose"));

        // 4. Default Seed Users
        // Student
        User studentUser = userRepository.save(new User("student@university.edu", passwordEncoder.encode(DEFAULT_SEED_PASSWORD), Role.STUDENT));
        StudentProfile student = studentProfileRepository.save(new StudentProfile(
                studentUser, "2100290120001", "Ayush Sharma", "CS", BigDecimal.valueOf(8.45), 2026
        ));

        // Add initial skills for student
        studentSkillRepository.save(new StudentSkill(student, dsa, BigDecimal.valueOf(0.85), true, "ASSESSMENT_VERIFIED"));
        studentSkillRepository.save(new StudentSkill(student, ood, BigDecimal.valueOf(0.80), true, "ASSESSMENT_VERIFIED"));
        studentSkillRepository.save(new StudentSkill(student, javaSpring, BigDecimal.valueOf(0.75), true, "LAB_ACADEMIC_EVALUATION"));
        studentSkillRepository.save(new StudentSkill(student, reactTs, BigDecimal.valueOf(0.70), true, "SELF_REPORTED"));

        // Recruiter
        User recruiterUser = userRepository.save(new User("recruiter@google.com", passwordEncoder.encode(DEFAULT_SEED_PASSWORD), Role.RECRUITER));
        Company google = companyRepository.save(new Company("Google India", "Cloud & Internet Services", "https://careers.google.com", "TIER_1"));
        RecruiterProfile recruiter = recruiterProfileRepository.save(new RecruiterProfile(
                recruiterUser, google, "Neha Verma", "Senior Technical Recruiter", "recruiter@google.com"
        ));

        // TPO Admin
        userRepository.save(new User("tpo@university.edu", passwordEncoder.encode(DEFAULT_SEED_PASSWORD), Role.TPO_ADMIN));

        // 5. Job Requisition with Skills
        JobRequisition req = new JobRequisition(
                google,
                recruiter,
                "Associate Software Development Engineer (SDE-1)",
                "Full-time campus hiring for Backend & Distributed Systems engineering team.",
                BigDecimal.valueOf(7.50),
                "FULL_TIME",
                BigDecimal.valueOf(18.50),
                LocalDateTime.now(ZoneId.systemDefault()).plusDays(30)
        );

        RequisitionSkill rs1 = new RequisitionSkill(req, dsa, 5, BigDecimal.valueOf(0.80));
        RequisitionSkill rs2 = new RequisitionSkill(req, javaSpring, 5, BigDecimal.valueOf(0.75));
        RequisitionSkill rs3 = new RequisitionSkill(req, postgres, 4, BigDecimal.valueOf(0.70));
        RequisitionSkill rs4 = new RequisitionSkill(req, docker, 3, BigDecimal.valueOf(0.60));
        RequisitionSkill rs5 = new RequisitionSkill(req, os, 4, BigDecimal.valueOf(0.70));

        req.setRequiredSkills(List.of(rs1, rs2, rs3, rs4, rs5));
        jobRequisitionRepository.save(req);
    }
}
