import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { reportAPI } from '../../services/api';
import toast from 'react-hot-toast';

export default function ManagerCompletions() {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = () => {
    reportAPI.getByStatus('PENDING')
      .then(r => setReports(r.data || []))
      .catch(() => toast.error('Failed to load'))
      .finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const handle = async (id, action) => {
    try {
      if (action === 'approve') await reportAPI.approve(id);
      else await reportAPI.reject(id);
      toast.success(action === 'approve' ? 'Case closed ✓' : 'Report rejected');
      load();
    } catch { toast.error('Failed'); }
  };

  return (
    <AppLayout pageTitle="Completion Reports">
      <div className="card-header" style={{ marginBottom: '1.25rem' }}>
        <h2>Pending Completion Reports</h2>
        <span className="badge badge-amber">{reports.length} pending</span>
      </div>

      {loading ? <div className="loading"><div className="spinner" /></div> : reports.length === 0 ? (
        <div className="empty-state"><p>No pending completion reports</p></div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
          {reports.map(r => (
            <div key={r.id} className="card">
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.75rem' }}>
                <div>
                  <div style={{ fontWeight: 500 }}>{r.technician?.fullName || '—'}</div>
                  <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', fontFamily: 'var(--font-mono)' }}>
                    Request #{r.request?.id} · {r.submittedAt ? new Date(r.submittedAt).toLocaleString() : '—'}
                  </div>
                </div>
                <span className="badge badge-amber">Pending</span>
              </div>

              <div style={{ background: 'var(--bg)', borderRadius: 6, padding: '10px 12px', marginBottom: '0.75rem' }}>
                <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.04em', marginBottom: 4 }}>Work Summary</div>
                <p style={{ fontSize: '0.875rem' }}>{r.workSummary || 'No summary provided'}</p>
              </div>

              <div style={{ display: 'flex', gap: 8 }}>
                <button className="btn btn-primary btn-sm" onClick={() => handle(r.id, 'approve')}>✓ Approve & Close</button>
                <button className="btn btn-danger btn-sm" onClick={() => handle(r.id, 'reject')}>Reject</button>
              </div>
            </div>
          ))}
        </div>
      )}
    </AppLayout>
  );
}
