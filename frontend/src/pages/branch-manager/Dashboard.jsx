import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { requestAPI, tokenAPI, reportAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';

export default function ManagerDashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ pending: 0, active: 0, pendingReports: 0 });
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const branchId = user?.branch?.id;
    if (!branchId) { setLoading(false); return; }
    Promise.all([
      requestAPI.getByBranch(branchId).catch(() => ({ data: [] })),
      tokenAPI.getByBranch(branchId).catch(() => ({ data: [] })),
      reportAPI.getByStatus('PENDING').catch(() => ({ data: [] })),
    ]).then(([reqs, tokens, rpts]) => {
      const allReqs = reqs.data || [];
      setStats({
        pending: allReqs.filter(r => r.status === 'PENDING').length,
        active: (tokens.data || []).filter(t => t.status === 'ACTIVE').length,
        pendingReports: (rpts.data || []).length,
      });
      setRequests(allReqs.slice(0, 8));
    }).finally(() => setLoading(false));
  }, []);

  const statusBadge = (s) => {
    const m = { PENDING: 'badge-amber', APPROVED: 'badge-green', REJECTED: 'badge-red', IN_PROGRESS: 'badge-blue', COMPLETED: 'badge-gray' };
    return <span className={`badge ${m[s] || 'badge-gray'}`}>{s}</span>;
  };

  const handleStatus = async (id, status) => {
    try {
      await requestAPI.updateStatus(id, status);
      toast.success(`Request ${status.toLowerCase()}`);
      setRequests(prev => prev.map(r => r.id === id ? { ...r, status } : r));
    } catch { toast.error('Failed to update'); }
  };

  return (
    <AppLayout pageTitle="Branch Dashboard">
      {loading ? <div className="loading"><div className="spinner" /></div> : (
        <>
          <div className="stat-grid">
            <div className="stat-card">
              <div className="stat-label">Pending Requests</div>
              <div className="stat-value" style={{ color: 'var(--amber)' }}>{stats.pending}</div>
              <div className="stat-sub">Awaiting review</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Active Tokens</div>
              <div className="stat-value" style={{ color: 'var(--accent-text)' }}>{stats.active}</div>
              <div className="stat-sub">Valid entries</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Pending Reports</div>
              <div className="stat-value">{stats.pendingReports}</div>
              <div className="stat-sub">Awaiting closure</div>
            </div>
          </div>

          <div className="card">
            <div className="card-header">
              <h2>Maintenance Requests</h2>
            </div>
            <div className="table-wrap">
              <table>
                <thead>
                  <tr><th>Technician</th><th>Issue</th><th>Priority</th><th>Status</th><th>Date</th><th>Actions</th></tr>
                </thead>
                <tbody>
                  {requests.map(r => (
                    <tr key={r.id}>
                      <td style={{ fontWeight: 500 }}>{r.technician?.fullName || '—'}</td>
                      <td style={{ maxWidth: 180, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', color: 'var(--text-secondary)', fontSize: '0.875rem' }}>{r.issueDescription}</td>
                      <td><span className={`priority-${r.priority}`} style={{ fontSize: '0.8rem', fontWeight: 500 }}>{r.priority}</span></td>
                      <td>{statusBadge(r.status)}</td>
                      <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{r.preferredVisitDate || '—'}</td>
                      <td>
                        {r.status === 'PENDING' && (
                          <div style={{ display: 'flex', gap: 6 }}>
                            <button className="btn btn-primary btn-sm" onClick={() => handleStatus(r.id, 'APPROVED')}>Approve</button>
                            <button className="btn btn-danger btn-sm" onClick={() => handleStatus(r.id, 'REJECTED')}>Reject</button>
                          </div>
                        )}
                      </td>
                    </tr>
                  ))}
                  {requests.length === 0 && (
                    <tr><td colSpan={6} style={{ textAlign: 'center', color: 'var(--text-muted)', padding: '2rem' }}>No requests found</td></tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}
    </AppLayout>
  );
}
