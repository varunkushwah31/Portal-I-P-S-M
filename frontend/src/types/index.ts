export type Role = 'STUDENT' | 'RECRUITER' | 'TPO_ADMIN';

export type ApplicationStage =
  | 'APPLIED'
  | 'SHORTLISTED'
  | 'TECHNICAL_ASSESSMENT'
  | 'INTERVIEW_ROUND'
  | 'OFFERED'
  | 'REJECTED';

export interface User {
  id: string;
  email: string;
  fullName: string;
  role: Role;
  profileId?: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  userId: string;
  email: string;
  fullName: string;
  role: Role;
  profileId?: string;
}

export interface Skill {
  id: string;
  canonicalName: string;
  category: string;
  description: string;
}

export interface StudentSkill {
  skillId: string;
  skillName: string;
  category: string;
  proficiencyLevel: number;
  isVerified: boolean;
  verificationSource: string;
}

export interface StudentProfile {
  id: string;
  userId: string;
  rollNumber: string;
  fullName: string;
  department: string;
  cgpa: number;
  graduationYear: number;
  isPlaced: boolean;
  skills: StudentSkill[];
}

export interface RequisitionSkill {
  skillId: string;
  skillName: string;
  weight: number;
  minProficiency: number;
}

export interface JobRequisition {
  id: string;
  companyName: string;
  title: string;
  description: string;
  minCgpa: number;
  jobType: 'INTERNSHIP' | 'FULL_TIME';
  status: 'DRAFT' | 'OPEN' | 'CLOSED';
  ctcLpa?: number;
  deadline: string;
  requiredSkills: RequisitionSkill[];
}

export interface CompatibilityReport {
  studentId: string;
  requisitionId: string;
  jobTitle: string;
  jaccardIndex: number;
  cosineSimilarity: number;
  compositeScore: number;
  matchedSkillsCount: number;
  totalRequiredSkillsCount: number;
  isEligible: boolean;
}

export interface MissingSkillItem {
  skillId: string;
  skillName: string;
  category: string;
  courseCode: string;
  courseName: string;
  remedialGuide: string;
}

export interface CompetencyGapReport {
  studentId: string;
  requisitionId: string;
  jobTitle: string;
  companyName: string;
  missingSkillsCount: number;
  missingSkills: MissingSkillItem[];
}

export interface NaacReport {
  academicYear: string;
  totalGraduatingStudents: number;
  totalPlacedStudents: number;
  placementPercentage: number;
  averagePackageLpa: number;
  totalCorporateDrives: number;
  topDeficitSkills: string[];
}

export interface AuditLog {
  id: string;
  actorId?: string;
  actorRole: string;
  actionType: string;
  entityName: string;
  entityId?: string;
  details: string;
  ipAddress?: string;
  recordedAt: string;
}
