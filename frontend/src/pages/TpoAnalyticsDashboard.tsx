import React, { useState, useEffect } from 'react';
import { apiClient } from '../api/client';
import { NaacReport, AuditLog } from '../types';
import { StatusBadge } from '../components/StatusBadge';
import {
  ShieldCheckIcon,
  ChartBarIcon,
  GraduationCapIcon,
  CurrencyInrIcon,
  BriefcaseIcon,
  WarningCircleIcon,
  ClockCounterClockwiseIcon,
  CheckCircleIcon,
  BuildingIcon
} from '@phosphor-icons/react';

export const TpoAnalyticsDashboard: React.FC = () => {
  const [naacData, setNaacData] = useState<NaacReport | null>(null);
  const [auditLogs, setAuditLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAnalytics();
  }, []);

  const fetchAnalytics = async () => {
    setLoading(true);
    try {
      const [naacRes, auditsRes] = await Promise.all([
        apiClient.get('/tpo/analytics/naac-summary', { params: { graduationYear: 2026 } }),
        apiClient.get('/tpo/audits', { params: { page: 0, size: 20 } })
      ]);
      setNaacData(naacRes.data);
      setAuditLogs(auditsRes.data.content || []);
    } catch (err) {
      console.error('Error fetching TPO analytics', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <div className="text-center space-y-2">
          <ChartBarIcon className="w-8 h-8 text-purple-600 animate-pulse mx-auto" />
          <p className="text-xs text-slate-500 font-medium">Aggregating NAAC/NBA accreditation records...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-8 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Header */}
      <div className="pb-4 border-b border-slate-200 flex justify-between items-center">
        <div>
          <div className="flex items-center space-x-2">
            <ShieldCheckIcon className="w-6 h-6 text-purple-700" weight="bold" />
            <h2 className="text-xl font-bold text-slate-900">Institutional Placement & Accreditation Console</h2>
          </div>
          <p className="text-xs text-slate-500 mt-0.5">
            NAAC Criterion 5 (Student Progression) &amp; NBA Criterion 4 Audit Aggregates — Department of Computer Science &amp; Engineering
          </p>
        </div>

        <span className="text-xs font-semibold px-3 py-1 bg-purple-50 text-purple-700 border border-purple-200 rounded-full font-mono">
          {naacData?.academicYear || 'AY 2025-2026'}
        </span>
      </div>

      {/* KPI Cards Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-5 space-y-1">
          <div className="flex items-center justify-between text-slate-500 text-xs">
            <span>Placement Rate</span>
            <ChartBarIcon className="w-4 h-4 text-emerald-600" weight="bold" />
          </div>
          <div className="text-2xl font-black font-mono text-slate-900">
            {naacData?.placementPercentage.toFixed(1)}%
          </div>
          <span className="text-[11px] text-slate-400 block">
            {naacData?.totalPlacedStudents} of {naacData?.totalGraduatingStudents} Eligible Candidates
          </span>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-5 space-y-1">
          <div className="flex items-center justify-between text-slate-500 text-xs">
            <span>Average Package</span>
            <CurrencyInrIcon className="w-4 h-4 text-brand-600" weight="bold" />
          </div>
          <div className="text-2xl font-black font-mono text-slate-900">
            INR {naacData?.averagePackageLpa.toFixed(2)} LPA
          </div>
          <span className="text-[11px] text-slate-400 block">
            Across campus & pooled drive offers
          </span>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-5 space-y-1">
          <div className="flex items-center justify-between text-slate-500 text-xs">
            <span>Corporate Drives</span>
            <BriefcaseIcon className="w-4 h-4 text-indigo-600" weight="bold" />
          </div>
          <div className="text-2xl font-black font-mono text-slate-900">
            {naacData?.totalCorporateDrives}
          </div>
          <span className="text-[11px] text-slate-400 block">
            Requisitions currently registered
          </span>
        </div>

        <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-5 space-y-1">
          <div className="flex items-center justify-between text-slate-500 text-xs">
            <span>Accreditation Status</span>
            <CheckCircleIcon className="w-4 h-4 text-emerald-600" weight="fill" />
          </div>
          <div className="text-2xl font-black font-mono text-slate-900">
            COMPLIANT
          </div>
          <span className="text-[11px] text-emerald-600 font-medium block">
            NAAC/NBA Verifiable Audit Stream
          </span>
        </div>
      </div>

      {/* Curriculum Deficit Analysis */}
      <div className="bg-white rounded-xl border border-slate-200 shadow-sm p-6 space-y-3">
        <div className="flex items-center space-x-2">
          <WarningCircleIcon className="w-5 h-5 text-amber-600" weight="bold" />
          <h3 className="text-sm font-bold text-slate-900">
            Top Corporate Skill Demands & Institutional Deficit Analysis
          </h3>
        </div>
        <p className="text-xs text-slate-500">
          Skills carrying the highest corporate requisition frequency across campus drives. Academic department review recommended:
        </p>
        <div className="flex flex-wrap gap-2 pt-1">
          {naacData?.topDeficitSkills.map((sk, idx) => (
            <span
              key={idx}
              className="px-3 py-1 bg-amber-50 text-amber-800 border border-amber-200 rounded-lg text-xs font-semibold"
            >
              #{idx + 1} {sk}
            </span>
          ))}
        </div>
      </div>

      {/* Immutable Audit Log Records */}
      <div className="bg-white rounded-xl border border-slate-200 shadow-sm overflow-hidden">
        <div className="p-5 border-b border-slate-200 flex items-center justify-between bg-slate-50">
          <div className="flex items-center space-x-2">
            <ClockCounterClockwiseIcon className="w-5 h-5 text-slate-700" weight="bold" />
            <h3 className="text-sm font-bold text-slate-900">Immutable Placement Audit Trail (Event Stream)</h3>
          </div>
          <span className="text-xs text-slate-500">
            Showing latest {auditLogs.length} events
          </span>
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-xs">
            <thead className="bg-slate-100 text-slate-600 uppercase font-semibold border-b border-slate-200">
              <tr>
                <th className="px-4 py-3">Timestamp</th>
                <th className="px-4 py-3">Role</th>
                <th className="px-4 py-3">Action Type</th>
                <th className="px-4 py-3">Entity</th>
                <th className="px-4 py-3">Event Details</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200">
              {auditLogs.length === 0 ? (
                <tr>
                  <td colSpan={5} className="text-center py-6 text-slate-500">
                    No placement audit records recorded yet.
                  </td>
                </tr>
              ) : (
                auditLogs.map((log) => (
                  <tr key={log.id} className="hover:bg-slate-50">
                    <td className="px-4 py-3 font-mono text-slate-500 whitespace-nowrap">
                      {new Date(log.recordedAt).toLocaleString()}
                    </td>
                    <td className="px-4 py-3 whitespace-nowrap">
                      <span className="px-2 py-0.5 rounded font-mono font-bold text-[10px] bg-slate-200 text-slate-800">
                        {log.actorRole}
                      </span>
                    </td>
                    <td className="px-4 py-3 font-mono font-semibold text-slate-800 whitespace-nowrap">
                      {log.actionType}
                    </td>
                    <td className="px-4 py-3 text-slate-600 whitespace-nowrap">
                      {log.entityName}
                    </td>
                    <td className="px-4 py-3 text-slate-700 font-mono text-[11px]">
                      {log.details}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};
