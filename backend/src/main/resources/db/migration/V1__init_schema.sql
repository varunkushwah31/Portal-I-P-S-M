-- V1__init_schema.sql: 3NF Relational Schema for Academia-Industry Placement & Skill Mapping System

CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE student_profiles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    roll_number VARCHAR(50) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL,
    department VARCHAR(100) NOT NULL,
    cgpa NUMERIC(4, 2) NOT NULL,
    graduation_year INT NOT NULL,
    is_placed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE companies (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    industry_domain VARCHAR(100) NOT NULL,
    website VARCHAR(255),
    tier VARCHAR(50) NOT NULL DEFAULT 'TIER_1',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE recruiter_profiles (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE RESTRICT,
    full_name VARCHAR(255) NOT NULL,
    designation VARCHAR(100) NOT NULL,
    corporate_email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE skill_categories (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE skills (
    id UUID PRIMARY KEY,
    canonical_name VARCHAR(150) NOT NULL UNIQUE,
    category_id UUID NOT NULL REFERENCES skill_categories(id) ON DELETE RESTRICT,
    description TEXT
);

CREATE TABLE student_skills (
    id UUID PRIMARY KEY,
    student_id UUID NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    skill_id UUID NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    proficiency_level NUMERIC(3, 2) NOT NULL CHECK (proficiency_level >= 0.0 AND proficiency_level <= 1.0),
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verification_source VARCHAR(100) DEFAULT 'SELF_REPORTED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_student_skill UNIQUE (student_id, skill_id)
);

CREATE TABLE job_requisitions (
    id UUID PRIMARY KEY,
    company_id UUID NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    recruiter_id UUID NOT NULL REFERENCES recruiter_profiles(id) ON DELETE RESTRICT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    min_cgpa NUMERIC(4, 2) NOT NULL DEFAULT 6.0,
    job_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    ctc_lpa NUMERIC(6, 2),
    deadline TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE requisition_skills (
    id UUID PRIMARY KEY,
    requisition_id UUID NOT NULL REFERENCES job_requisitions(id) ON DELETE CASCADE,
    skill_id UUID NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    weight INT NOT NULL CHECK (weight >= 1 AND weight <= 5),
    min_proficiency NUMERIC(3, 2) NOT NULL DEFAULT 0.50,
    CONSTRAINT uq_req_skill UNIQUE (requisition_id, skill_id)
);

CREATE TABLE applications (
    id UUID PRIMARY KEY,
    requisition_id UUID NOT NULL REFERENCES job_requisitions(id) ON DELETE CASCADE,
    student_id UUID NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    compatibility_score NUMERIC(5, 4) NOT NULL,
    current_stage VARCHAR(50) NOT NULL DEFAULT 'APPLIED',
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_application UNIQUE (requisition_id, student_id)
);

CREATE TABLE course_curriculum_mappings (
    id UUID PRIMARY KEY,
    skill_id UUID NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    course_code VARCHAR(50) NOT NULL,
    course_name VARCHAR(255) NOT NULL,
    semester INT NOT NULL,
    department VARCHAR(50) NOT NULL DEFAULT 'CS',
    remedial_guide TEXT,
    CONSTRAINT uq_skill_course UNIQUE (skill_id, course_code)
);

CREATE TABLE competency_gap_reports (
    id UUID PRIMARY KEY,
    application_id UUID REFERENCES applications(id) ON DELETE CASCADE,
    student_id UUID NOT NULL REFERENCES student_profiles(id) ON DELETE CASCADE,
    requisition_id UUID NOT NULL REFERENCES job_requisitions(id) ON DELETE CASCADE,
    missing_skills_count INT NOT NULL,
    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE competency_gap_items (
    id UUID PRIMARY KEY,
    gap_report_id UUID NOT NULL REFERENCES competency_gap_reports(id) ON DELETE CASCADE,
    skill_id UUID NOT NULL REFERENCES skills(id) ON DELETE CASCADE,
    course_code VARCHAR(50),
    course_name VARCHAR(255),
    remedial_recommendation TEXT
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    actor_id UUID,
    actor_role VARCHAR(50) NOT NULL,
    action_type VARCHAR(100) NOT NULL,
    entity_name VARCHAR(100) NOT NULL,
    entity_id UUID,
    details TEXT,
    ip_address VARCHAR(45),
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_student_skills_student ON student_skills(student_id);
CREATE INDEX idx_student_skills_skill ON student_skills(skill_id);
CREATE INDEX idx_req_skills_req ON requisition_skills(requisition_id);
CREATE INDEX idx_applications_req ON applications(requisition_id);
CREATE INDEX idx_applications_student ON applications(student_id);
CREATE INDEX idx_audit_logs_actor ON audit_logs(actor_id);
CREATE INDEX idx_audit_logs_recorded ON audit_logs(recorded_at);
