import React, { useState, useEffect } from 'react';
import { apiClient } from '../api/client';
import { JobRequisition, Skill, ApplicationStage } from '../types';
import { StatusBadge } from '../components/StatusBadge';
import {
  Briefcase,
  Plus,
  Users,
  CheckCircle,
  XCircle,
  Lightning,
  Buildings,
  Sliders,
  CalendarBlank,
  CurrencyInr
} from '@phosphor-icons/react';

export const RecruiterConsole: React.FC = () => {
  const [requisitions, setRequisitions] = useState<JobRequisition[]>([]);
  const [allSkills, setAllSkills] = useState<Skill[]>([]);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [loading, setLoading] = useState(true);

  // Form State
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [minCgpa, setMinCgpa] = useState(7.0);
  const [jobType, setJobType] = useState<'INTERNSHIP' | 'FULL_TIME'>('FULL_TIME');
  const [ctcLpa, setCtcLpa] = useState(12.0);
  const [selectedSkillWeights, setSelectedSkillWeights] = useState<Record<string, { weight: number; minProficiency: number }>>({});
  const [creating, setCreating] = useState(false);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [reqsRes, skillsRes] = await Promise.all([
        apiClient.get('/recruiter/requisitions'),
        apiClient.get('/skills')
      ]);
      setRequisitions(reqsRes.data);
      setAllSkills(skillsRes.data);
    } catch (err) {
      console.error('Error fetching recruiter console data', err);
    } finally {
      setLoading(false);
    }
  };

  const toggleSkillSelection = (skillId: string) => {
    setSelectedSkillWeights(prev => {
      const copy = { ...prev };
      if (copy[skillId]) {
        delete copy[skillId];
      } else {
        copy[skillId] = { weight: 4, minProficiency: 0.70 };
      }
      return copy;
    });
  };

  const updateSkillParam = (skillId: string, field: 'weight' | 'minProficiency', val: number) => {
    setSelectedSkillWeights(prev => ({
      ...prev,
      [skillId]: {
        ...prev[skillId],
        [field]: val
      }
    }));
  };

  const handleCreateRequisition = async (e: React.FormEvent) => {
    e.preventDefault();
    setCreating(true);

    try {
      const skillsPayload = Object.entries(selectedSkillWeights).map(([skillId, cfg]) => ({
        skillId,
        weight: cfg.weight,
        minProficiency: cfg.minProficiency
      }));

      const deadline = new Date();
      deadline.setDate(deadline.getDate() + 30);

      await apiClient.post('/recruiter/requisitions', {
        title,
        description,
        minCgpa,
        jobType,
        ctcLpa,
        deadline: deadline.toISOString(),
        skills: skillsPayload
      });

      setShowCreateModal(false);
      setTitle('');
      setDescription('');
      setSelectedSkillWeights({});
      fetchData();
    } catch (err) {
      console.error('Failed to create requisition', err);
      alert('Error publishing job requisition. Please verify inputs.');
    } finally {
      setCreating(false);
    }
  };

  return (
    <div className="space-y-6 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Top Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 pb-4 border-b border-slate-200">
        <div>
          <h2 className="text-xl font-bold text-slate-900">Corporate Recruiter Console</h2>
          <p className="text-xs text-slate-500">
            Configure weighted technical skill requirements and evaluate deterministic applicant compatibility.
          </p>
        </div>

        <button
          onClick={() => setShowCreateModal(true)}
          className="inline-flex items-center px-4 py-2 bg-slate-900 hover:bg-slate-800 text-white text-xs font-semibold rounded-lg shadow-sm transition-colors"
        >
          <Plus className="w-4 h-4 mr-1.5" weight="bold" />
          Create New Job Requisition
        </button>
      </div>

      {/* Requisitions List */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {requisitions.map((req) => (
          <div key={req.id} className="bg-white rounded-xl border border-slate-200 shadow-sm p-5 space-y-4">
            <div className="flex justify-between items-start">
              <div>
                <span className="text-xs font-semibold text-brand-700 bg-brand-50 px-2 py-0.5 rounded border border-brand-200">
                  {req.companyName}
                </span>
                <h3 className="text-base font-bold text-slate-900 mt-1">{req.title}</h3>
              </div>
              <StatusBadge status={req.status} />
            </div>

            <p className="text-xs text-slate-600 line-clamp-3">{req.description}</p>

            <div className="grid grid-cols-2 gap-2 text-xs bg-slate-50 p-3 rounded-lg border border-slate-200">
              <div>
                <span className="text-slate-500 block">Compensation</span>
                <span className="font-mono font-bold text-slate-800">
                  {req.ctcLpa ? `INR ${req.ctcLpa} LPA` : 'Competitive'}
                </span>
              </div>
              <div>
                <span className="text-slate-500 block">Min Academic CGPA</span>
                <span className="font-mono font-bold text-slate-800">{req.minCgpa.toFixed(1)} / 10.0</span>
              </div>
            </div>

            <div>
              <span className="text-xs font-semibold text-slate-700 block mb-2">
                Weighted Skill Taxonomy Criteria:
              </span>
              <div className="flex flex-wrap gap-1.5">
                {req.requiredSkills?.map((rs) => (
                  <span
                    key={rs.skillId}
                    className="inline-flex items-center text-[11px] px-2 py-0.5 bg-slate-100 text-slate-700 rounded-md border border-slate-200"
                  >
                    <span className="font-medium mr-1">{rs.skillName}</span>
                    <span className="text-brand-700 font-bold font-mono">w={rs.weight}</span>
                  </span>
                ))}
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Modal for Creating Requisition */}
      {showCreateModal && (
        <div className="fixed inset-0 z-50 overflow-y-auto bg-slate-900/60 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-white rounded-xl shadow-2xl border border-slate-200 max-w-2xl w-full max-h-[90vh] flex flex-col overflow-hidden animate-in fade-in duration-200">
            <div className="p-5 border-b border-slate-200 flex items-center justify-between bg-slate-50">
              <div className="flex items-center space-x-2">
                <Briefcase className="w-5 h-5 text-brand-700" weight="bold" />
                <h3 className="text-base font-bold text-slate-900">Publish Corporate Job Requisition</h3>
              </div>
              <button
                onClick={() => setShowCreateModal(false)}
                className="text-slate-400 hover:text-slate-600 text-sm font-bold"
              >
                Cancel
              </button>
            </div>

            <form onSubmit={handleCreateRequisition} className="p-6 overflow-y-auto space-y-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Role Title</label>
                <input
                  type="text"
                  required
                  placeholder="e.g. SDE-1 (Backend & Cloud Engineering)"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  className="w-full text-xs p-2.5 border border-slate-300 rounded-lg focus:ring-brand-500 focus:border-brand-500"
                />
              </div>

              <div>
                <label className="block text-xs font-semibold text-slate-700 mb-1">Job Description</label>
                <textarea
                  required
                  rows={3}
                  placeholder="Detail the technical responsibilities and team mandate..."
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  className="w-full text-xs p-2.5 border border-slate-300 rounded-lg focus:ring-brand-500 focus:border-brand-500"
                />
              </div>

              <div className="grid grid-cols-3 gap-3">
                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Min CGPA</label>
                  <input
                    type="number"
                    step="0.1"
                    min="5.0"
                    max="10.0"
                    value={minCgpa}
                    onChange={(e) => setMinCgpa(parseFloat(e.target.value))}
                    className="w-full text-xs p-2 border border-slate-300 rounded-lg"
                  />
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">Type</label>
                  <select
                    value={jobType}
                    onChange={(e) => setJobType(e.target.value as any)}
                    className="w-full text-xs p-2 border border-slate-300 rounded-lg bg-white"
                  >
                    <option value="FULL_TIME">Full Time</option>
                    <option value="INTERNSHIP">Internship</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-semibold text-slate-700 mb-1">CTC (LPA)</label>
                  <input
                    type="number"
                    step="0.5"
                    value={ctcLpa}
                    onChange={(e) => setCtcLpa(parseFloat(e.target.value))}
                    className="w-full text-xs p-2 border border-slate-300 rounded-lg"
                  />
                </div>
              </div>

              {/* Taxonomy Skill Assignment */}
              <div className="pt-2">
                <span className="block text-xs font-semibold text-slate-700 mb-2">
                  Select Required Skills and Assign Weights (1=Optional to 5=Mandatory):
                </span>
                <div className="space-y-2 max-h-48 overflow-y-auto border border-slate-200 rounded-lg p-2 bg-slate-50">
                  {allSkills.map((sk) => {
                    const isSelected = !!selectedSkillWeights[sk.id];
                    const cfg = selectedSkillWeights[sk.id];

                    return (
                      <div
                        key={sk.id}
                        className={`p-2.5 rounded-md border text-xs transition-colors ${
                          isSelected
                            ? 'bg-brand-50 border-brand-300'
                            : 'bg-white border-slate-200'
                        }`}
                      >
                        <div className="flex items-center justify-between">
                          <label className="flex items-center space-x-2 cursor-pointer">
                            <input
                              type="checkbox"
                              checked={isSelected}
                              onChange={() => toggleSkillSelection(sk.id)}
                              className="rounded text-brand-600 focus:ring-brand-500"
                            />
                            <span className="font-semibold text-slate-800">{sk.canonicalName}</span>
                          </label>

                          {isSelected && (
                            <div className="flex items-center space-x-3">
                              <div className="flex items-center space-x-1">
                                <span className="text-[11px] text-slate-500">Weight:</span>
                                <select
                                  value={cfg.weight}
                                  onChange={(e) => updateSkillParam(sk.id, 'weight', parseInt(e.target.value))}
                                  className="text-xs p-1 border border-slate-300 rounded bg-white font-mono font-bold"
                                >
                                  <option value={1}>1 (Low)</option>
                                  <option value={2}>2</option>
                                  <option value={3}>3 (Med)</option>
                                  <option value={4}>4</option>
                                  <option value={5}>5 (Mandatory)</option>
                                </select>
                              </div>
                            </div>
                          )}
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>

              <div className="pt-4 border-t border-slate-200 flex justify-end space-x-2">
                <button
                  type="button"
                  onClick={() => setShowCreateModal(false)}
                  className="px-4 py-2 text-xs font-semibold text-slate-600 hover:bg-slate-100 rounded-lg"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={creating || Object.keys(selectedSkillWeights).length === 0}
                  className="px-4 py-2 text-xs font-semibold bg-slate-900 text-white hover:bg-slate-800 rounded-lg disabled:opacity-50"
                >
                  {creating ? 'Publishing Requisition...' : 'Publish Requisition'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};
