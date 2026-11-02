package com.academia.placement.service;

import com.academia.placement.config.JwtTokenProvider;
import com.academia.placement.dto.AuthResponse;
import com.academia.placement.dto.LoginRequest;
import com.academia.placement.dto.RegisterRequest;
import com.academia.placement.model.*;
import com.academia.placement.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuditService auditService;

    public AuthService(UserRepository userRepository,
                       StudentProfileRepository studentProfileRepository,
                       RecruiterProfileRepository recruiterProfileRepository,
                       CompanyRepository companyRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider,
                       AuditService auditService) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.auditService = auditService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email already exists: " + request.getEmail());
        }

        User user = new User(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole()
        );
        user = userRepository.save(user);

        UUID profileId = null;

        if (request.getRole() == Role.STUDENT) {
            if (studentProfileRepository.existsByRollNumber(request.getRollNumber())) {
                throw new IllegalArgumentException("Roll number already registered: " + request.getRollNumber());
            }

            StudentProfile student = new StudentProfile(
                    user,
                    request.getRollNumber(),
                    request.getFullName(),
                    request.getDepartment() != null ? request.getDepartment() : "CS",
                    request.getCgpa() != null ? request.getCgpa() : BigDecimal.valueOf(7.5),
                    request.getGraduationYear() != null ? request.getGraduationYear() : 2026
            );
            student = studentProfileRepository.save(student);
            profileId = student.getId();

        } else if (request.getRole() == Role.RECRUITER) {
            String compName = request.getCompanyName() != null ? request.getCompanyName() : "Partner Enterprise";
            Company company = companyRepository.findByName(compName)
                    .orElseGet(() -> companyRepository.save(new Company(compName, "Software Engineering", null, "TIER_1")));

            RecruiterProfile recruiter = new RecruiterProfile(
                    user,
                    company,
                    request.getFullName(),
                    request.getDesignation() != null ? request.getDesignation() : "Talent Acquisition Lead",
                    request.getEmail()
            );
            recruiter = recruiterProfileRepository.save(recruiter);
            profileId = recruiter.getId();
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        auditService.recordEvent(
                user.getId(),
                user.getRole().name(),
                "USER_REGISTERED",
                "User",
                user.getId(),
                "Registered new user account: " + user.getEmail(),
                "0.0.0.0"
        );

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                request.getFullName(),
                user.getRole(),
                profileId
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        UUID profileId = null;
        String fullName = user.getEmail();

        if (user.getRole() == Role.STUDENT) {
            StudentProfile student = studentProfileRepository.findByUserId(user.getId()).orElse(null);
            if (student != null) {
                profileId = student.getId();
                fullName = student.getFullName();
            }
        } else if (user.getRole() == Role.RECRUITER) {
            RecruiterProfile recruiter = recruiterProfileRepository.findByUserId(user.getId()).orElse(null);
            if (recruiter != null) {
                profileId = recruiter.getId();
                fullName = recruiter.getFullName();
            }
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole().name());

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                fullName,
                user.getRole(),
                profileId
        );
    }
}

