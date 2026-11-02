-- V2__seed_taxonomy_and_curriculum.sql: Normalized Skill Taxonomy and Curriculum Mappings

-- Skill Categories
INSERT INTO skill_categories (id, name, description) VALUES
('c0000001-0000-0000-0000-000000000001', 'Core Computer Science', 'Fundamental algorithmic and systems theory'),
('c0000001-0000-0000-0000-000000000002', 'Data Persistence & Storage', 'Relational, NoSQL, and cache storage architectures'),
('c0000001-0000-0000-0000-000000000003', 'Systems & Cloud Infrastructure', 'Operating systems, containerization, and cloud primitives'),
('c0000001-0000-0000-0000-000000000004', 'Enterprise Web Architecture', 'Backend REST/gRPC frameworks, middleware, and security');

-- Skills
INSERT INTO skills (id, canonical_name, category_id, description) VALUES
-- Core CS
('s0000001-0000-0000-0000-000000000001', 'Data Structures & Algorithms', 'c0000001-0000-0000-0000-000000000001', 'Trees, graphs, dynamic programming, asymptotic complexity analysis'),
('s0000001-0000-0000-0000-000000000002', 'Object-Oriented Design', 'c0000001-0000-0000-0000-000000000001', 'SOLID principles, design patterns, domain modeling'),
('s0000001-0000-0000-0000-000000000003', 'Operating Systems Primitives', 'c0000001-0000-0000-0000-000000000001', 'Process scheduling, concurrency, virtual memory, IPC'),

-- Storage
('s0000001-0000-0000-0000-000000000004', 'PostgreSQL & Relational Modeling', 'c0000001-0000-0000-0000-000000000002', '3NF normalization, ACID transactions, index optimization, query plans'),
('s0000001-0000-0000-0000-000000000005', 'Redis Caching & In-Memory Stores', 'c0000001-0000-0000-0000-000000000002', 'Cache-aside patterns, key eviction, TTL management, pub-sub'),

-- Systems & Cloud
('s0000001-0000-0000-0000-000000000006', 'Docker & Containerization', 'c0000001-0000-0000-0000-000000000003', 'Multi-stage builds, container isolation, docker-compose orchestration'),
('s0000001-0000-0000-0000-000000000007', 'Computer Networks & Protocols', 'c0000001-0000-0000-0000-000000000003', 'TCP/IP, HTTP/1.1-2-3, DNS, TLS termination, socket programming'),

-- Web Architecture
('s0000001-0000-0000-0000-000000000008', 'Java & Spring Boot 3', 'c0000001-0000-0000-0000-000000000004', 'Spring MVC, Spring Data JPA, Spring Security, dependency injection'),
('s0000001-0000-0000-0000-000000000009', 'RESTful API Engineering', 'c0000001-0000-0000-0000-000000000004', 'Stateless JWT auth, idempotency, status code semantics, API contracts'),
('s0000001-0000-0000-0000-000000000010', 'React & TypeScript', 'c0000001-0000-0000-0000-000000000004', 'Component lifecycle, hooks, strict type safety, responsive layout');

-- Academic Coursework Mapping (Computer Science Curriculum)
INSERT INTO course_curriculum_mappings (id, skill_id, course_code, course_name, semester, department, remedial_guide) VALUES
('m0000001-0000-0000-0000-000000000001', 's0000001-0000-0000-0000-000000000001', 'CS-301', 'Data Structures & Algorithms', 3, 'CS', 'Complete lab modules on Graph Traversal, Balanced Trees, and Dynamic Programming'),
('m0000001-0000-0000-0000-000000000002', 's0000001-0000-0000-0000-000000000003', 'CS-401', 'Operating Systems', 4, 'CS', 'Review process synchronization, mutex locks, semaphores, and paging algorithms'),
('m0000001-0000-0000-0000-000000000003', 's0000001-0000-0000-0000-000000000004', 'CS-501', 'Database Management Systems', 5, 'CS', 'Practice normal form decompositions (BCNF/3NF), indexing B+ trees, and transaction isolation levels'),
('m0000001-0000-0000-0000-000000000004', 's0000001-0000-0000-0000-000000000007', 'CS-601', 'Computer Networks', 6, 'CS', 'Analyze packet captures with Wireshark and implement multi-threaded socket servers in Java'),
('m0000001-0000-0000-0000-000000000005', 's0000001-0000-0000-0000-000000000008', 'CS-701', 'Enterprise Web Architectures', 7, 'CS', 'Build multi-tier Spring Boot applications with Spring Security JWT and Hibernate JPA');
