import React from 'react';
import {
  CheckCircle,
  XCircle,
  Clock,
  ShieldCheck,
  Lightning,
  Sparkle
} from '@phosphor-icons/react';

interface StatusBadgeProps {
  status: string;
  size?: 'sm' | 'md';
}

export const StatusBadge: React.FC<StatusBadgeProps> = ({ status, size = 'sm' }) => {
  const norm = status.toUpperCase();

  const getStyleAndIcon = () => {
    switch (norm) {
      case 'OFFERED':
      case 'ELIGIBLE':
      case 'VERIFIED':
      case 'OPEN':
        return {
          bg: 'bg-emerald-50 text-emerald-700 border-emerald-200',
          icon: <CheckCircle className="w-3.5 h-3.5 mr-1 text-emerald-600" weight="bold" />,
          label: norm
        };
      case 'REJECTED':
      case 'CLOSED':
      case 'INELIGIBLE':
        return {
          bg: 'bg-rose-50 text-rose-700 border-rose-200',
          icon: <XCircle className="w-3.5 h-3.5 mr-1 text-rose-600" weight="bold" />,
          label: norm
        };
      case 'SHORTLISTED':
      case 'INTERVIEW_ROUND':
      case 'TECHNICAL_ASSESSMENT':
        return {
          bg: 'bg-indigo-50 text-indigo-700 border-indigo-200',
          icon: <Lightning className="w-3.5 h-3.5 mr-1 text-indigo-600" weight="bold" />,
          label: norm.replace('_', ' ')
        };
      case 'TIER_1':
        return {
          bg: 'bg-amber-50 text-amber-700 border-amber-200',
          icon: <Sparkle className="w-3.5 h-3.5 mr-1 text-amber-600" weight="fill" />,
          label: 'Tier 1'
        };
      case 'APPLIED':
      case 'DRAFT':
      default:
        return {
          bg: 'bg-slate-100 text-slate-700 border-slate-200',
          icon: <Clock className="w-3.5 h-3.5 mr-1 text-slate-500" weight="bold" />,
          label: norm.replace('_', ' ')
        };
    }
  };

  const config = getStyleAndIcon();
  const padding = size === 'sm' ? 'px-2 py-0.5 text-xs' : 'px-3 py-1 text-sm';

  return (
    <span className={`inline-flex items-center font-medium rounded-full border ${config.bg} ${padding}`}>
      {config.icon}
      {config.label}
    </span>
  );
};
