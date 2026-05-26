import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { requestAPI, tokenAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';

export default function TechnicianDashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ pending: 0, approved: 0, activeTokens: 0 });
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      requestAPI.getByTechnician(user?.id).catch(() => ({ data: [] })),
      tokenAPI.getByTechnician(user?.id).catch(() => ({ data: [] })),
    ]).then(([reqs, tokens]) => {
      const all = reqs.data || [];
      setStats({
        pending: all.filter(r => r.status === 'PENDING').length,
        approved: all.filter(r => r.status === 'APPROVED').length,
        activeTokens: (tokens.data || []).filter(t => t.status === 'ACTIVE').length,
      });
      setRequests(all.slice(0, 5));
    }).finally(() => setLoading(false));
  }, []);

  const statusBadge = (s) => {
    const m = { PENDING: 'badge-amber', APPROVED: 'badge-green', REJECTED: 'badge-red', IN_PROGRESS: 'badge-blue', COMPLETED: 'badge-gray' };
    return <span className={`badge ${m[s] || 'badge-gray'}`}>{s}</span>;
  };

  return (
    <AppLayout pageTitle="My Dashboard">
      {loading ? <div className="loading"><div className="spinner" /></div> : (
        <>
          <div style={{ marginBottom: '1rem' }}>
            <h1 style={{ fontSize: '1.2rem', fontWeight: 500 }}>Welcome back, {user?.fullName?.split(' ')[0]}</h1>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>Branch: {user?.branch?.name || 'Not assigned'}</p>
          </div>

          <div className="stat-grid">
            <div className="stat-card">
              <div className="stat-label">Pending Requests</div>
              <div className="stat-value" style={{ color: 'var(--amber)' }}>{stats.pending}</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Approved</div>
              <div className="stat-value" style={{ color: 'var(--accent-text)' }}>{stats.approved}</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Active Tokens</div>
              <div className="stat-value">{stats.activeTokens}</div>
            </div>
          </div>

          <div className="card">
            <div className="card-header"><h2>Recent Requests</h2></div>
            <div className="table-wrap">
              <table>
                <thead><tr><th>Branch</th><th>Priority</th><th>Status</th><th>Preferred Date</th></tr></thead>
                <tbody>
                  {requests.map(r => (
                    <tr key={r.id}>
                      <td>{r.branch?.name || '—'}</td>
                      <td><span className={`priority-${r.priority}`} style={{ fontSize: '0.82rem', fontWeight: 500 }}>{r.priority}</span></td>
                      <td>{statusBadge(r.status)}</td>
                      <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{r.preferredVisitDate || '—'}</td>
                    </tr>
                  ))}
                  {requests.length === 0 && <tr><td colSpan={4} style={{ textAlign: 'center', color: 'var(--text-muted)', padding: '2rem' }}>No requests yet</td></tr>}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}
    </AppLayout>
  );
}
