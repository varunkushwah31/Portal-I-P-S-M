import React from 'react';
import { CompetencyGapReport } from '../types';
import {
  X,
  BookOpen,
  WarningCircle,
  Lightbulb,
  CheckCircle,
  Buildings
} from '@phosphor-icons/react';

interface CompetencyGapModalProps {
  report: CompetencyGapReport | null;
  onClose: () => void;
}

export const CompetencyGapModal: React.FC<CompetencyGapModalProps> = ({ report, onClose }) => {
  if (!report) return null;

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto bg-slate-900/60 backdrop-blur-sm flex items-center justify-center p-4">
      <div className="bg-white rounded-xl shadow-2xl border border-slate-200 max-w-2xl w-full max-h-[90vh] flex flex-col overflow-hidden animate-in fade-in duration-200">
        
        {/* Header */}
        <div className="p-5 border-b border-slate-200 flex items-center justify-between bg-slate-50">
          <div className="flex items-center space-x-3">
            <div className="p-2 bg-amber-50 text-amber-700 border border-amber-200 rounded-lg">
              <WarningCircle className="w-5 h-5" weight="bold" />
            </div>
            <div>
              <h2 className="text-lg font-bold text-slate-900">
                Competency Gap Diagnostic Report
              </h2>
              <p className="text-xs text-slate-500">
                Target Role: <span className="font-semibold text-slate-700">{report.jobTitle}</span> ({report.companyName})
              </p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-1.5 text-slate-400 hover:text-slate-600 rounded-lg hover:bg-slate-200/60 transition-colors"
          >
            <X className="w-5 h-5" weight="bold" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 overflow-y-auto space-y-4">
          <div className="bg-sky-50 border border-sky-200 rounded-lg p-4 flex items-start space-x-3">
            <Lightbulb className="w-5 h-5 text-sky-700 shrink-0 mt-0.5" weight="duotone" />
            <div className="text-xs text-sky-900 space-y-1">
              <p className="font-semibold">Automated Academic Curriculum Mapping</p>
              <p>
                Our deterministic engine matched your profile against the technical requisition and isolated{' '}
                <span className="font-bold text-sky-800">{report.missingSkillsCount} prerequisite competencies</span>.
                Below are the institutional CS curriculum courses and remedial lab modules recommended to bridge these gaps.
              </p>
            </div>
          </div>

          {report.missingSkills.length === 0 ? (
            <div className="text-center py-8">
              <CheckCircle className="w-12 h-12 text-emerald-500 mx-auto mb-2" weight="duotone" />
              <h3 className="text-sm font-semibold text-slate-800">Complete Skill Alignment</h3>
              <p className="text-xs text-slate-500 mt-1">
                You satisfy all prerequisite competencies required for this role requisition.
              </p>
            </div>
          ) : (
            <div className="space-y-3">
              {report.missingSkills.map((item) => (
                <div
                  key={item.skillId}
                  className="p-4 rounded-lg border border-slate-200 bg-white hover:border-slate-300 transition-colors shadow-sm"
                >
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-sm font-bold text-slate-900">
                      {item.skillName}
                    </span>
                    <span className="text-xs px-2 py-0.5 bg-slate-100 text-slate-700 rounded-md border border-slate-200 font-medium">
                      {item.category}
                    </span>
                  </div>

                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 mt-3 pt-3 border-t border-slate-100 text-xs">
                    <div className="flex items-start space-x-2">
                      <BookOpen className="w-4 h-4 text-brand-600 shrink-0 mt-0.5" weight="bold" />
                      <div>
                        <span className="text-slate-500 block">Mapped Coursework:</span>
                        <span className="font-semibold text-slate-800">
                          {item.courseCode} - {item.courseName}
                        </span>
                      </div>
                    </div>

                    <div className="flex items-start space-x-2">
                      <Buildings className="w-4 h-4 text-slate-500 shrink-0 mt-0.5" weight="bold" />
                      <div>
                        <span className="text-slate-500 block">Recommended Remediation:</span>
                        <span className="text-slate-700">
                          {item.remedialGuide}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="p-4 border-t border-slate-200 bg-slate-50 flex justify-end">
          <button
            onClick={onClose}
            className="px-4 py-2 text-xs font-semibold bg-slate-800 text-white hover:bg-slate-900 rounded-lg transition-colors"
          >
            Close Report
          </button>
        </div>
      </div>
    </div>
  );
};
