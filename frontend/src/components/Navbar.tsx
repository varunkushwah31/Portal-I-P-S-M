import React from 'react';
import { useAuth } from '../context/AuthContext';
import {
  GraduationCap,
  Briefcase,
  ShieldCheck,
  SignOut,
  Buildings
} from '@phosphor-icons/react';

export const Navbar: React.FC = () => {
  const { user, logout } = useAuth();

  const getRoleBadge = () => {
    switch (user?.role) {
      case 'STUDENT':
        return {
          icon: <GraduationCap className="w-4 h-4 mr-1 text-sky-600" weight="bold" />,
          label: 'Student',
          color: 'bg-sky-50 text-sky-700 border-sky-200'
        };
      case 'RECRUITER':
        return {
          icon: <Briefcase className="w-4 h-4 mr-1 text-emerald-600" weight="bold" />,
          label: 'Corporate Recruiter',
          color: 'bg-emerald-50 text-emerald-700 border-emerald-200'
        };
      case 'TPO_ADMIN':
        return {
          icon: <ShieldCheck className="w-4 h-4 mr-1 text-purple-600" weight="bold" />,
          label: 'TPO Administrator',
          color: 'bg-purple-50 text-purple-700 border-purple-200'
        };
      default:
        return {
          icon: null,
          label: 'User',
          color: 'bg-slate-100 text-slate-700 border-slate-200'
        };
    }
  };

  const badge = getRoleBadge();

  return (
    <header className="bg-white border-b border-slate-200 sticky top-0 z-30">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        <div className="flex items-center space-x-3">
          <div className="p-2 bg-brand-50 border border-brand-200 rounded-lg text-brand-700">
            <Buildings className="w-6 h-6" weight="duotone" />
          </div>
          <div>
            <h1 className="text-base font-bold text-slate-900 leading-tight">
              KIET Deemed to be University
            </h1>
            <p className="text-xs text-slate-500 font-medium">
              Department of Computer Science - Skill Mapping & Placement Portal
            </p>
          </div>
        </div>

        {user && (
          <div className="flex items-center space-x-4">
            <div className="flex items-center space-x-2">
              <span className={`inline-flex items-center text-xs px-2.5 py-1 rounded-full border font-medium ${badge.color}`}>
                {badge.icon}
                {badge.label}
              </span>
              <span className="text-sm font-semibold text-slate-800">
                {user.fullName}
              </span>
            </div>

            <button
              onClick={logout}
              className="inline-flex items-center px-3 py-1.5 text-xs font-medium text-slate-600 hover:text-rose-600 bg-slate-50 hover:bg-rose-50 border border-slate-200 hover:border-rose-200 rounded-md transition-colors"
            >
              <SignOut className="w-3.5 h-3.5 mr-1" weight="bold" />
              Sign Out
            </button>
          </div>
        )}
      </div>
    </header>
  );
};
