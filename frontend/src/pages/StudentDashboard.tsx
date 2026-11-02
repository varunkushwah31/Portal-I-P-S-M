import React, { useState, useEffect } from 'react';
import { apiClient } from '../api/client';
import {
  StudentProfile,
  JobRequisition,
  CompatibilityReport,
  CompetencyGapReport,
  Skill
} from '../types';
import { StatusBadge } from '../components/StatusBadge';
import { CompetencyGapModal } from '../components/CompetencyGapModal';
import {
  Sliders,
  CheckCircle,
  Briefcase,
  WarningCircle,
  Buildings,
  ChartBar,
  Percent,
  Cpu,
  GraduationCap
} from '@phosphor-icons/react';

export const StudentDashboard: React.FC = () => {
  const [profile, setProfile] = useState<StudentProfile | null>(null);
  const [allSkills, setAllSkills] = useState<Skill[]>([]);
  const [requisitions, setRequisitions] = useState<JobRequisition[]>([]);
  const [compatibilities, setCompatibilities] = useState<Record<string, CompatibilityReport>>({});
  const [activeGapReport, setActiveGapReport] = useState<CompetencyGapReport | null>(null);
  const [loading, setLoading] = useState(true);
  const [applyingId, setApplyingId] = useState<string | null>(null);
  const [applicationSuccess, setApplicationSuccess] = useState<string | null>(null);

  // Skill matrix edit state
  const [editingSkills, setEditingSkills] = useState<Record<string, number>>({});
  const [savingSkills, setSavingSkills] = useState(false);

  useEffect(() => {
    fetchInitialData();
  }, []);

  const fetchInitialData = async () => {
    setLoading(true);
    try {
      const [profileRes, skillsRes, reqsRes] = await Promise.all([
        apiClient.get('/students/me'),
        apiClient.get('/skills'),
        apiClient.get('/recruiter/requisitions'),
      ]);

      const prof: StudentProfile = profileRes.data;
      setProfile(prof);
      setAllSkills(skillsRes.data);
      setRequisitions(reqsRes.data);

      // Pre-fill edit skills
      const initialMap: Record<string, number> = {};
      prof.skills.forEach((s) => {
        initialMap[s.skillId] = s.proficiencyLevel;
      });
      setEditingSkills(initialMap);

      // Fetch compatibility for each requisition
      const compMap: Record<string, CompatibilityReport> = {};
      for (const req of reqsRes.data) {
        try {
          const compRes = await apiClient.get('/matching/compatibility', {
            params: { studentId: prof.id, requisitionId: req.id },
          });
          compMap[req.id] = compRes.data;
        } catch (e) {
          console.error('Error fetching compatibility for req', req.id, e);
        }
      }
      setCompatibilities(compMap);
    } catch (err) {
      console.error('Failed to load student dashboard data', err);
    } finally {
      setLoading(false);
    }
  };

  const handleProficiencyChange = (skillId: string, value: number) => {
    setEditingSkills((prev) => ({
      ...prev,
      [skillId]: value,
    }));
  };

  const handleSaveSkillMatrix = async () => {
    if (!profile) return;
    setSavingSkills(true);
    try {
      const payload = Object.entries(editingSkills)
        .filter(([_, level]) => level > 0)
        .map(([skillId, level]) => ({
          skillId,
          proficiencyLevel: level,
        }));

      const res = await apiClient.put('/students/me/skills', payload);
      setProfile(res.data);

      // Re-calculate compatibility across requisitions
      const compMap: Record<string, CompatibilityReport> = {};
      for (const req of requisitions) {
        const compRes = await apiClient.get('/matching/compatibility', {
          params: { studentId: res.data.id, requisitionId: req.id },
        });
        compMap[req.id] = compRes.data;
      }
      setCompatibilities(compMap);
    } catch (err) {
      console.error('Failed to update skill matrix', err);
    } finally {
      setSavingSkills(false);
    }
  };

  const handleOpenGapAnalysis = async (requisitionId: string) => {
    if (!profile) return;
    try {
      const res = await apiClient.get('/matching/gap-analysis', {
        params: { studentId: profile.id, requisitionId },
      });
      setActiveGapReport(res.data);
    } catch (err) {
      console.error('Failed to generate gap report', err);
    }
  };

  const handleApply = async (requisitionId: string) => {
    setApplyingId(requisitionId);
    setApplicationSuccess(null);
    try {
      await apiClient.post('/matching/apply', null, {
        params: { requisitionId },
      });
      setApplicationSuccess(requisitionId);
    } catch (err: any) {
      alert(err.response?.data?.message || 'Error submitting application.');
    } finally {
      setApplyingId(null);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="text-center space-y-2">
          <Cpu className="w-8 h-8 text-brand-600 animate-pulse mx-auto" />
          <p className="text-xs text-slate-500 font-medium">Computing deterministic compatibility matrices...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Student Profile Summary Card */}
      {profile && (
        <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-6">
          <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
            <div className="flex items-center space-x-4">
              <div className="w-14 h-14 rounded-full bg-slate-900 text-white flex items-center justify-center font-bold text-lg">
                <GraduationCap className="w-7 h-7" weight="duotone" />
              </div>
              <div>
                <div className="flex items-center space-x-2">
                  <h2 className="text-xl font-bold text-slate-900">{profile.fullName}</h2>
                  <StatusBadge status={profile.isPlaced ? 'OFFERED' : 'APPLIED'} />
                </div>
                <p className="text-xs text-slate-500 mt-0.5">
                  Roll No: <span className="font-mono text-slate-700">{profile.rollNumber}</span> | Department of {profile.department} (Class of {profile.graduationYear})
                </p>
              </div>
            </div>

            <div className="flex items-center space-x-6 border-t md:border-t-0 pt-4 md:pt-0 border-slate-100">
              <div className="text-right">
                <span className="text-xs text-slate-500 block">Cumulative GPA</span>
                <span className="text-2xl font-black text-slate-900 font-mono">{profile.cgpa.toFixed(2)}</span>
              </div>
              <div className="text-right">
                <span className="text-xs text-slate-500 block">Verified Skills</span>
                <span className="text-2xl font-black text-brand-600 font-mono">{profile.skills.length}</span>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Main Grid: Skill Matrix on Left, Job Matching on Right */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        
        {/* Interactive Skill Matrix Editor */}
        <div className="lg:col-span-5 bg-white rounded-xl border border-slate-200 shadow-sm p-6 space-y-4">
          <div className="flex items-center justify-between pb-3 border-b border-slate-100">
            <div className="flex items-center space-x-2">
              <Sliders className="w-5 h-5 text-brand-600" weight="bold" />
              <h3 className="text-sm font-bold text-slate-900">Technical Skill Taxonomy Matrix</h3>
            </div>
            <button
              onClick={handleSaveSkillMatrix}
              disabled={savingSkills}
              className="px-3 py-1 text-xs font-semibold bg-slate-900 text-white hover:bg-slate-800 rounded-md transition-colors disabled:opacity-50"
            >
              {savingSkills ? 'Persisting...' : 'Save Matrix'}
            </button>
          </div>

          <p className="text-xs text-slate-500">
            Adjust verified proficiency levels (0.0 to 1.0) according to academic coursework and laboratory evaluations.
          </p>

          <div className="space-y-4 max-h-[500px] overflow-y-auto pr-1">
            {allSkills.map((sk) => {
              const currentVal = editingSkills[sk.id] ?? 0;
              return (
                <div key={sk.id} className="p-3 bg-slate-50 rounded-lg border border-slate-200 text-xs space-y-2">
                  <div className="flex justify-between items-center">
                    <div>
                      <span className="font-semibold text-slate-800 block">{sk.canonicalName}</span>
                      <span className="text-[11px] text-slate-500">{sk.category}</span>
                    </div>
                    <span className="font-mono font-bold text-brand-700 bg-brand-50 px-2 py-0.5 rounded border border-brand-200">
                      {(currentVal * 100).toFixed(0)}%
                    </span>
                  </div>

                  <input
                    type="range"
                    min="0"
                    max="1"
                    step="0.05"
                    value={currentVal}
                    onChange={(e) => handleProficiencyChange(sk.id, parseFloat(e.target.value))}
                    className="w-full accent-brand-600 cursor-pointer h-1.5 bg-slate-200 rounded-lg"
                  />
                </div>
              );
            })}
          </div>
        </div>

        {/* Requisition Matching & Deterministic Scoring */}
        <div className="lg:col-span-7 space-y-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-2">
              <Briefcase className="w-5 h-5 text-slate-700" weight="bold" />
              <h3 className="text-sm font-bold text-slate-900">Corporate Requisitions & Deterministic Match</h3>
            </div>
            <span className="text-xs text-slate-500 font-medium">
              {requisitions.length} active corporate requisitions
            </span>
          </div>

          <div className="space-y-4">
            {requisitions.map((req) => {
              const comp = compatibilities[req.id];
              const isApplied = applicationSuccess === req.id;

              return (
                <div key={req.id} className="bg-white rounded-xl border border-slate-200 shadow-sm p-5 space-y-4 hover:border-slate-300 transition-colors">
                  <div className="flex items-start justify-between">
                    <div>
                      <div className="flex items-center space-x-2">
                        <span className="text-xs font-semibold text-brand-700 bg-brand-50 px-2 py-0.5 rounded border border-brand-200">
                          {req.companyName}
                        </span>
                        <StatusBadge status={req.jobType} />
                      </div>
                      <h4 className="text-base font-bold text-slate-900 mt-1">{req.title}</h4>
                      <p className="text-xs text-slate-500 line-clamp-2 mt-0.5">{req.description}</p>
                    </div>

                    <div className="text-right">
                      {req.ctcLpa && (
                        <span className="text-sm font-bold text-slate-900 font-mono block">
                          INR {req.ctcLpa.toFixed(1)} LPA
                        </span>
                      )}
                      <span className="text-[11px] text-slate-500 block">Min CGPA: {req.minCgpa.toFixed(1)}</span>
                    </div>
                  </div>

                  {/* Deterministic Mathematical Breakdown */}
                  {comp && (
                    <div className="bg-slate-50 rounded-lg p-3 border border-slate-200 grid grid-cols-3 gap-2 text-center text-xs">
                      <div>
                        <span className="text-[11px] text-slate-500 block">Jaccard Index</span>
                        <span className="font-mono font-bold text-slate-800">
                          {(comp.jaccardIndex * 100).toFixed(1)}%
                        </span>
                        <span className="text-[10px] text-slate-400 block mt-0.5">Set Overlap</span>
                      </div>

                      <div>
                        <span className="text-[11px] text-slate-500 block">Cosine Similarity</span>
                        <span className="font-mono font-bold text-slate-800">
                          {(comp.cosineSimilarity * 100).toFixed(1)}%
                        </span>
                        <span className="text-[10px] text-slate-400 block mt-0.5">Weighted Vector</span>
                      </div>

                      <div>
                        <span className="text-[11px] text-slate-500 block">Composite Score</span>
                        <span className="font-mono font-black text-brand-700">
                          {(comp.compositeScore * 100).toFixed(1)}%
                        </span>
                        <div className="mt-0.5">
                          <StatusBadge status={comp.isEligible ? 'ELIGIBLE' : 'INELIGIBLE'} />
                        </div>
                      </div>
                    </div>
                  )}

                  {/* Action Buttons */}
                  <div className="flex items-center justify-between pt-2 border-t border-slate-100">
                    <button
                      onClick={() => handleOpenGapAnalysis(req.id)}
                      className="inline-flex items-center text-xs font-semibold text-brand-700 hover:text-brand-800 hover:underline"
                    >
                      <WarningCircle className="w-4 h-4 mr-1 text-amber-600" weight="bold" />
                      View Competency Gap Report
                    </button>

                    <button
                      onClick={() => handleApply(req.id)}
                      disabled={isApplied || applyingId === req.id || (comp && !comp.isEligible)}
                      className={`inline-flex items-center px-4 py-2 text-xs font-semibold rounded-lg transition-colors ${
                        isApplied
                          ? 'bg-emerald-100 text-emerald-800 border border-emerald-300'
                          : comp && !comp.isEligible
                          ? 'bg-slate-100 text-slate-400 cursor-not-allowed border border-slate-200'
                          : 'bg-slate-900 text-white hover:bg-slate-800'
                      }`}
                    >
                      {isApplied ? (
                        <>
                          <CheckCircle className="w-4 h-4 mr-1 text-emerald-600" weight="bold" />
                          Application Submitted
                        </>
                      ) : applyingId === req.id ? (
                        'Submitting...'
                      ) : comp && !comp.isEligible ? (
                        'Criteria Unmet'
                      ) : (
                        'Submit Placement Application'
                      )}
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>

      {/* Competency Gap Diagnostic Modal */}
      <CompetencyGapModal
        report={activeGapReport}
        onClose={() => setActiveGapReport(null)}
      />
    </div>
  );
};
