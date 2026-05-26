import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { userAPI, companyAPI, branchAPI, requestAPI } from '../../services/api';

export default function AdminDashboard() {
  const [stats, setStats] = useState({ users: 0, companies: 0, branches: 0, pendingRequests: 0 });
  const [recentRequests, setRecentRequests] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      userAPI.getAll().catch(() => ({ data: [] })),
      companyAPI.getAll().catch(() => ({ data: [] })),
      branchAPI.getAll().catch(() => ({ data: [] })),
      requestAPI.getAll().catch(() => ({ data: [] })),
    ]).then(([users, companies, branches, requests]) => {
      const allReqs = requests.data || [];
      setStats({
        users: (users.data || []).length,
        companies: (companies.data || []).length,
        branches: (branches.data || []).length,
        pendingRequests: allReqs.filter(r => r.status === 'PENDING').length,
      });
      setRecentRequests(allReqs.slice(0, 5));
    }).finally(() => setLoading(false));
  }, []);

  const statusBadge = (s) => {
    const map = { PENDING: 'badge-amber', APPROVED: 'badge-green', REJECTED: 'badge-red', IN_PROGRESS: 'badge-blue', COMPLETED: 'badge-gray' };
    return <span className={`badge ${map[s] || 'badge-gray'}`}>{s}</span>;
  };

  return (
    <AppLayout pageTitle="Dashboard">
      {loading ? <div className="loading"><div className="spinner" /></div> : (
        <>
          <div className="stat-grid">
            <div className="stat-card">
              <div className="stat-label">Total Users</div>
              <div className="stat-value">{stats.users}</div>
              <div className="stat-sub">Across all branches</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Companies</div>
              <div className="stat-value">{stats.companies}</div>
              <div className="stat-sub">Registered organizations</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Branches</div>
              <div className="stat-value">{stats.branches}</div>
              <div className="stat-sub">Active locations</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Pending</div>
              <div className="stat-value" style={{ color: 'var(--amber)' }}>{stats.pendingRequests}</div>
              <div className="stat-sub">Maintenance requests</div>
            </div>
          </div>

          <div className="card">
            <div className="card-header">
              <h2>Recent Maintenance Requests</h2>
            </div>
            {recentRequests.length === 0 ? (
              <div className="empty-state">
                <p>No maintenance requests yet</p>
              </div>
            ) : (
              <div className="table-wrap">
                <table>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Branch</th>
                      <th>Priority</th>
                      <th>Status</th>
                      <th>Submitted</th>
                    </tr>
                  </thead>
                  <tbody>
                    {recentRequests.map(r => (
                      <tr key={r.id}>
                        <td className="text-mono" style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>#{r.id}</td>
                        <td>{r.branch?.name || '—'}</td>
                        <td><span className={`priority-${r.priority}`} style={{ fontSize: '0.82rem', fontWeight: 500 }}>{r.priority}</span></td>
                        <td>{statusBadge(r.status)}</td>
                        <td style={{ color: 'var(--text-muted)', fontSize: '0.82rem' }}>
                          {r.submittedAt ? new Date(r.submittedAt).toLocaleDateString() : '—'}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        </>
      )}
    </AppLayout>
  );
}
