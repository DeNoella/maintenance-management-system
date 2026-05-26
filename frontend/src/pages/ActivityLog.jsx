import { useEffect, useState } from 'react';
import AppLayout from '../components/layout/AppLayout';
import { activityAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';

export default function ActivityLog({ adminView = false }) {
  const { user } = useAuth();
  const [logs, setLogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('');

  useEffect(() => {
    const fetch = adminView ? activityAPI.getAll() : activityAPI.getByUser(user?.id);
    fetch
      .then(r => setLogs(r.data || []))
      .catch(() => toast.error('Failed to load activity'))
      .finally(() => setLoading(false));
  }, []);

  const filtered = logs.filter(l =>
    !filter || l.actionType?.toLowerCase().includes(filter.toLowerCase()) || l.entityType?.toLowerCase().includes(filter.toLowerCase())
  );

  const actionColor = (a) => {
    if (!a) return 'badge-gray';
    if (a.includes('CREATE')) return 'badge-green';
    if (a.includes('DEACTIVATE') || a.includes('DELETE') || a.includes('REJECT')) return 'badge-red';
    if (a.includes('UPDATE') || a.includes('APPROVE')) return 'badge-blue';
    return 'badge-amber';
  };

  return (
    <AppLayout pageTitle="Activity Log">
      <div className="card-header" style={{ marginBottom: '1.25rem' }}>
        <h2>{adminView ? 'System Activity' : 'My Activity'}</h2>
        <input
          className="form-input"
          style={{ width: 220 }}
          placeholder="Filter by action or entity…"
          value={filter}
          onChange={e => setFilter(e.target.value)}
        />
      </div>

      {loading ? <div className="loading"><div className="spinner" /></div> : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                {adminView && <th>User</th>}
                <th>Action</th>
                <th>Entity</th>
                <th>Details</th>
                <th>Date & Time</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(l => (
                <tr key={l.id}>
                  {adminView && <td style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>{l.user?.fullName || '—'}</td>}
                  <td><span className={`badge ${actionColor(l.actionType)}`}>{l.actionType?.replace(/_/g, ' ')}</span></td>
                  <td style={{ fontFamily: 'var(--font-mono)', fontSize: '0.8rem', color: 'var(--text-muted)' }}>{l.entityType}</td>
                  <td style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', maxWidth: 240 }}>{l.details}</td>
                  <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)', fontFamily: 'var(--font-mono)', whiteSpace: 'nowrap' }}>
                    {l.performedAt ? new Date(l.performedAt).toLocaleString() : '—'}
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr><td colSpan={adminView ? 5 : 4} style={{ textAlign: 'center', color: 'var(--text-muted)', padding: '2rem' }}>No activity recorded</td></tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </AppLayout>
  );
}
