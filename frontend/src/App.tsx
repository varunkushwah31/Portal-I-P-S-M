import React, { useState } from 'react';
import { useAuth } from './context/AuthContext';
import { LoginPage } from './pages/LoginPage';
import { StudentDashboard } from './pages/StudentDashboard';
import { RecruiterConsole } from './pages/RecruiterConsole';
import { TpoAnalyticsDashboard } from './pages/TpoAnalyticsDashboard';
import { Navbar } from './components/Navbar';
import {
  GraduationCap,
  Briefcase,
  ShieldCheck
} from '@phosphor-icons/react';

export const AppContent: React.FC = () => {
  const { user, isAuthenticated } = useAuth();
  const [activeTabOverride, setActiveTabOverride] = useState<string | null>(null);

  if (!isAuthenticated || !user) {
    return <LoginPage />;
  }

  // Active view: user's primary role or evaluator tab override
  const currentView = activeTabOverride || user.role;

  return (
    <div className="min-h-screen bg-slate-100 flex flex-col text-slate-900">
      <Navbar />

      {/* Role View Switcher Navigation Bar (For Examiner & Multi-Role Evaluation) */}
      <div className="bg-white border-b border-slate-200">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center space-x-1 py-2">
            <span className="text-xs font-semibold text-slate-500 mr-2">
              Evaluation Views:
            </span>

            <button
              onClick={() => setActiveTabOverride('STUDENT')}
              className={`inline-flex items-center px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors ${
                currentView === 'STUDENT'
                  ? 'bg-sky-100 text-sky-800 border border-sky-300'
                  : 'text-slate-600 hover:bg-slate-100'
              }`}
            >
              <GraduationCap className="w-4 h-4 mr-1 text-sky-600" weight="bold" />
              Student Portfolio & Gap Analysis
            </button>

            <button
              onClick={() => setActiveTabOverride('RECRUITER')}
              className={`inline-flex items-center px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors ${
                currentView === 'RECRUITER'
                  ? 'bg-emerald-100 text-emerald-800 border border-emerald-300'
                  : 'text-slate-600 hover:bg-slate-100'
              }`}
            >
              <Briefcase className="w-4 h-4 mr-1 text-emerald-600" weight="bold" />
              Recruiter Requisitions & Criteria
            </button>

            <button
              onClick={() => setActiveTabOverride('TPO_ADMIN')}
              className={`inline-flex items-center px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors ${
                currentView === 'TPO_ADMIN'
                  ? 'bg-purple-100 text-purple-800 border border-purple-300'
                  : 'text-slate-600 hover:bg-slate-100'
              }`}
            >
              <ShieldCheck className="w-4 h-4 mr-1 text-purple-600" weight="bold" />
              TPO Audits & NAAC/NBA Compliance
            </button>
          </div>
        </div>
      </div>

      {/* Main View Container */}
      <main className="flex-1 pb-12">
        {currentView === 'STUDENT' && <StudentDashboard />}
        {currentView === 'RECRUITER' && <RecruiterConsole />}
        {currentView === 'TPO_ADMIN' && <TpoAnalyticsDashboard />}
      </main>
    </div>
  );
};
