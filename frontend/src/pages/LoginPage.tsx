import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { apiClient } from '../api/client';
import {
  GraduationCap,
  Briefcase,
  ShieldCheck,
  Buildings,
  Lock,
  EnvelopeSimple,
  ArrowRight
} from '@phosphor-icons/react';

export const LoginPage: React.FC = () => {
  const { login } = useAuth();
  const [email, setEmail] = useState('student@kiet.edu');
  const [password, setPassword] = useState('Password123!');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const response = await apiClient.post('/auth/login', { email, password });
      login(response.data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Authentication failed. Please verify your credentials.');
    } finally {
      setLoading(false);
    }
  };

  const setDemoRole = (roleEmail: string) => {
    setEmail(roleEmail);
    setPassword('Password123!');
    setError(null);
  };

  return (
    <div className="min-h-screen bg-slate-100 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div className="sm:mx-auto sm:w-full sm:max-w-md text-center">
        <div className="inline-flex p-3 bg-white border border-slate-200 rounded-2xl shadow-sm mb-4 text-brand-600">
          <Buildings className="w-10 h-10" weight="duotone" />
        </div>
        <h2 className="text-2xl font-extrabold text-slate-900 tracking-tight">
          KIET Deemed to be University
        </h2>
        <p className="mt-1 text-sm text-slate-600">
          Academia-Industry Collaboration & Skill Mapping Portal
        </p>
        <span className="inline-block mt-2 text-xs font-semibold px-2.5 py-1 bg-brand-50 text-brand-700 border border-brand-200 rounded-full">
          SIH Problem ID: SIH26043
        </span>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-6 shadow-xl shadow-slate-200/50 sm:rounded-xl sm:px-10 border border-slate-200">
          
          {/* Quick Demo Fill Buttons */}
          <div className="mb-6">
            <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider block mb-2">
              Select Demo Role Persona
            </span>
            <div className="grid grid-cols-3 gap-2">
              <button
                type="button"
                onClick={() => setDemoRole('student@kiet.edu')}
                className={`p-2 text-xs font-medium rounded-lg border text-center transition-all ${
                  email === 'student@kiet.edu'
                    ? 'bg-sky-50 border-sky-300 text-sky-800 font-semibold'
                    : 'bg-slate-50 border-slate-200 text-slate-700 hover:bg-slate-100'
                }`}
              >
                <GraduationCap className="w-4 h-4 mx-auto mb-1 text-sky-600" weight="bold" />
                Student
              </button>

              <button
                type="button"
                onClick={() => setDemoRole('recruiter@google.com')}
                className={`p-2 text-xs font-medium rounded-lg border text-center transition-all ${
                  email === 'recruiter@google.com'
                    ? 'bg-emerald-50 border-emerald-300 text-emerald-800 font-semibold'
                    : 'bg-slate-50 border-slate-200 text-slate-700 hover:bg-slate-100'
                }`}
              >
                <Briefcase className="w-4 h-4 mx-auto mb-1 text-emerald-600" weight="bold" />
                Recruiter
              </button>

              <button
                type="button"
                onClick={() => setDemoRole('tpo@kiet.edu')}
                className={`p-2 text-xs font-medium rounded-lg border text-center transition-all ${
                  email === 'tpo@kiet.edu'
                    ? 'bg-purple-50 border-purple-300 text-purple-800 font-semibold'
                    : 'bg-slate-50 border-slate-200 text-slate-700 hover:bg-slate-100'
                }`}
              >
                <ShieldCheck className="w-4 h-4 mx-auto mb-1 text-purple-600" weight="bold" />
                TPO Admin
              </button>
            </div>
          </div>

          {error && (
            <div className="mb-4 p-3 bg-rose-50 border border-rose-200 rounded-lg text-xs text-rose-700 font-medium">
              {error}
            </div>
          )}

          <form className="space-y-4" onSubmit={handleSubmit}>
            <div>
              <label className="block text-xs font-medium text-slate-700">
                Institutional / Corporate Email
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-slate-400">
                  <EnvelopeSimple className="w-4 h-4" />
                </div>
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  className="block w-full pl-9 pr-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-brand-500 focus:border-brand-500 bg-white"
                  placeholder="name@kiet.edu"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-medium text-slate-700">
                Password
              </label>
              <div className="mt-1 relative rounded-md shadow-sm">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none text-slate-400">
                  <Lock className="w-4 h-4" />
                </div>
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                  className="block w-full pl-9 pr-3 py-2 text-sm border border-slate-300 rounded-lg focus:ring-brand-500 focus:border-brand-500 bg-white"
                />
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full flex justify-center items-center py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-xs font-semibold text-white bg-slate-900 hover:bg-slate-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-slate-900 transition-colors disabled:opacity-50"
            >
              {loading ? (
                'Authenticating...'
              ) : (
                <>
                  Authenticate & Enter Portal
                  <ArrowRight className="w-4 h-4 ml-1.5" weight="bold" />
                </>
              )}
            </button>
          </form>

          <div className="mt-6 pt-4 border-t border-slate-100 text-center">
            <span className="text-[11px] text-slate-400">
              Deterministic Skill Matching Engine &copy; KIET CS Dept 2026
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};
