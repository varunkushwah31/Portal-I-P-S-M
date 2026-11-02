# Academia–Industry Collaboration Portal for Skill Mapping, Internships, and Placement

**Smart India Hackathon Reference:** Problem ID SIH26043 (Govt. of Jharkhand)  
**Institution:** KIET Deemed to be University, Delhi-NCR, Ghaziabad (Department of Computer Science)  
**Academic Alignment:** Major Project Annexure I / B.Tech Final Year Capstone (CO1–CO5, SDG 4, 8, 9)  
**Stack Paradigm:** Latest Production Versions (Java 21/25 LTS, Spring Boot 3.4.x, PostgreSQL 17, Redis 7.4, React + Vite 6, TypeScript 5.7+, Tailwind CSS)

---

## 1. System Overview

Traditional university placement management relies on static spreadsheets, unindexed email chains, and opaque commercial ATS scanners. This system resolves these institutional deficiencies through:

1. **Role-Segregated Enterprise Architecture:** Segregated operational dashboards for **Students**, **Corporate Recruiters**, and **Training & Placement Officers (TPO / Administrators)** with stateless JSON Web Token (JWT) role-based access control.
2. **Deterministic Skill-Taxonomy Matching Engine:** Replaces heuristic black-box resume screening with a transparent, algorithmic combination of **Jaccard Set Intersection** and **Weighted Cosine Similarity** over a canonical skill taxonomy.
3. **Automated Competency Gap Diagnostic Reporting:** Isolates prerequisite competencies missing from unplaced candidates and deterministically maps them to institutional academic coursework (e.g., KCS-301, KCS-401, KCS-501, KCS-071) with specific remediation guides.
4. **Immutable Placement Audit Stream:** Generates verifiable, queryable placement records and aggregate metrics strictly compliant with **NAAC Criterion 5** and **NBA Criterion 4 & 5** accreditation reviews.

---

## 2. Mathematical Formulations

### 2.1 Jaccard Similarity (Set-Theoretic Skill Overlap)
Measures the proportion of required prerequisite technical skills possessed by the student:

$$\text{Jaccard}(S_{\text{student}}, S_{\text{requisition}}) = \frac{|S_{\text{student}} \cap S_{\text{requisition}}|}{|S_{\text{student}} \cup S_{\text{requisition}}|}$$

### 2.2 Weighted Cosine Similarity (Proficiency Alignment)
Incorporates recruiter-defined skill priority weights $w_i \in [1, 5]$ and target vs. actual candidate proficiency vectors:

$$\text{Cosine}(V_{\text{student}}, V_{\text{requisition}}) = \frac{\sum_{i=1}^{n} V_{\text{student}, i} \cdot V_{\text{requisition}, i}}{\sqrt{\sum_{i=1}^{n} (V_{\text{student}, i})^2} \cdot \sqrt{\sum_{i=1}^{n} (V_{\text{requisition}, i})^2}}$$

### 2.3 Composite Score & Eligibility
$$\text{Composite Score} = 0.40 \cdot \text{Jaccard} + 0.60 \cdot \text{Cosine}$$
$$\text{Eligibility} = (\text{CGPA} \ge \text{Min CGPA}) \land (\text{Composite Score} \ge 0.50)$$

---

## 3. Technology Stack

- **Backend:** Java 21 / 25 LTS, Spring Boot 3.4.3, Spring Security 6.4, Spring Data JPA, Hibernate 6.6, Flyway 10, Springdoc OpenAPI 2.8.5.
- **Persistence & Caching:** PostgreSQL 17 (`postgres:17-alpine`), Redis 7.4 (`redis:7.4-alpine`).
- **Frontend:** React 18, TypeScript 5.7+, Vite 6, Tailwind CSS, `@phosphor-icons/react` (strictly zero emojis, clean SVG tokens only).
- **Quality Assurance:** JUnit 5, Mockito, AssertJ, GitHub Actions CI.

---

## 4. Repository Layout

```text
d:/Final Year Project/
├── .github/workflows/ci.yml       # GitHub Actions automated build & test CI
├── docker-compose.yml             # PostgreSQL 17, Redis 7.4, and PgAdmin services
├── contracts/
│   └── openapi.yaml               # OpenAPI 3.1 REST API specification
├── backend/                       # Spring Boot 3.4.x core REST server
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/kiet/placement/
│       │   ├── config/            # SecurityConfig, JwtTokenProvider, OpenApiConfig, DataInitializer
│       │   ├── controller/        # AuthController, StudentController, RecruiterController, MatchingController, TpoController
│       │   ├── model/             # 3NF JPA Entities: User, StudentProfile, JobRequisition, Skill, Application, AuditLog
│       │   ├── repository/        # Spring Data JPA repositories
│       │   ├── service/           # MatchingEngineService, GapAnalysisService, AuditService, AuthService
│       │   └── dto/               # Strongly-typed request/response payloads
│       ├── main/resources/
│       │   ├── application.yml
│       │   └── db/migration/      # Flyway SQL migrations (V1 3NF schema, V2 seed taxonomy)
│       └── test/                  # JUnit 5 & Mockito test suites
└── frontend/                      # React 18 + TypeScript + Vite 6 client application
    ├── package.json
    ├── vite.config.ts
    ├── src/
    │   ├── api/client.ts          # Axios client with automatic Bearer token injection
    │   ├── context/AuthContext.tsx# Authentication state management
    │   ├── components/            # StatusBadge, Navbar, CompetencyGapModal
    │   ├── pages/                 # LoginPage, StudentDashboard, RecruiterConsole, TpoAnalyticsDashboard
    │   └── types/                 # Shared TypeScript models
```

---

## 5. Getting Started

### 5.1 Prerequisites
- Java 21 or Java 25 installed (`java -version`)
- Apache Maven 3.9+ installed (`mvn -version`)
- Node.js 20+ and npm installed (`node -v`)
- Docker & Docker Compose (optional; backend includes in-memory H2 PostgreSQL profile for instant evaluation)

### 5.2 Starting Infrastructure (Optional Docker)
```powershell
docker compose up -d
```
Starts PostgreSQL 17 on port 5432, Redis 7.4 on port 6379, and PgAdmin on port 5050.

### 5.3 Running the Backend Server
```powershell
cd backend
mvn spring-boot:run
```
- Server starts on: `http://localhost:8080`
- Interactive Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Specification: `http://localhost:8080/v3/api-docs`

### 5.4 Running the Frontend Client
```powershell
cd frontend
npm install
npm run dev
```
- Web Application: `http://localhost:5173`

---

## 6. Pre-Configured Personas for Evaluation

| Role Persona | Email | Password | Primary Functions |
|---|---|---|---|
| **Student** | `student@kiet.edu` | `Password123!` | Technical skill matrix adjustments, deterministic role compatibility check, curriculum gap analysis modal, application submission |
| **Recruiter** | `recruiter@google.com` | `Password123!` | Requisition publishing, skill weight criteria configuration, candidate recruitment pipeline |
| **TPO Administrator** | `tpo@kiet.edu` | `Password123!` | NAAC Criterion 5 placement statistics, top deficit skills analysis, immutable event audit stream |

---

## 7. Verification Results

- **Backend Unit Tests:** All tests passing (`MatchingEngineServiceTest`, `GapAnalysisServiceTest`)
  ```text
  [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
  [INFO] BUILD SUCCESS
  ```
- **Frontend Production Build:** Clean compilation with Vite 6 & TypeScript 5.7+
  ```text
  dist/index.html                   0.79 kB
  dist/assets/index-DHFzqhnr.css   22.24 kB
  dist/assets/index-ZUIiBWhZ.js   302.85 kB
  built in 18.23s
  ```
