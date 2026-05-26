import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { reportAPI, requestAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';

export default function TechnicianCompletions() {
  const { user } = useAuth();
  const [reports, setReports] = useState([]);
  const [eligibleRequests, setEligibleRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [form, setForm] = useState({ requestId: '', workSummary: '' });
  const [saving, setSaving] = useState(false);

  const load = () => {
    Promise.all([
      reportAPI.getByTechnician(user?.id).catch(() => ({ data: [] })),
      requestAPI.getByTechnician(user?.id).catch(() => ({ data: [] })),
    ]).then(([r, reqs]) => {
      setReports(r.data || []);
      const reported = new Set((r.data || []).map(rp => rp.request?.id));
      setEligibleRequests((reqs.data || []).filter(req => req.status === 'APPROVED' && !reported.has(req.id)));
    }).finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await reportAPI.submit({ ...form, technicianId: user?.id });
      toast.success('Completion report submitted');
      setShowModal(false);
      setForm({ requestId: '', workSummary: '' });
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to submit');
    } finally { setSaving(false); }
  };

  const statusBadge = (s) => {
    const m = { PENDING: 'badge-amber', APPROVED: 'badge-green', REJECTED: 'badge-red' };
    return <span className={`badge ${m[s] || 'badge-gray'}`}>{s}</span>;
  };

  return (
    <AppLayout pageTitle="Completion Reports">
      <div className="card-header" style={{ marginBottom: '1.25rem' }}>
        <h2>Completion Reports</h2>
        {eligibleRequests.length > 0 && (
          <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>+ Submit Report</button>
        )}
      </div>

      {loading ? <div className="loading"><div className="spinner" /></div> : reports.length === 0 ? (
        <div className="empty-state">
          <p>No completion reports yet.</p>
          {eligibleRequests.length > 0 && (
            <button className="btn btn-primary" style={{ marginTop: '1rem' }} onClick={() => setShowModal(true)}>Submit your first report</button>
          )}
        </div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead><tr><th>Request #</th><th>Branch</th><th>Work Summary</th><th>Status</th><th>Submitted</th></tr></thead>
            <tbody>
              {reports.map(r => (
                <tr key={r.id}>
                  <td className="text-mono" style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>#{r.request?.id}</td>
                  <td>{r.request?.branch?.name || '—'}</td>
                  <td style={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', fontSize: '0.875rem', color: 'var(--text-secondary)' }}>{r.workSummary}</td>
                  <td>{statusBadge(r.approvalStatus)}</td>
                  <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{r.submittedAt ? new Date(r.submittedAt).toLocaleDateString() : '—'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={e => { if (e.target === e.currentTarget) setShowModal(false); }}>
          <div className="modal">
            <div className="modal-header">
              <h3>Submit Completion Report</h3>
              <button className="btn btn-secondary btn-sm" onClick={() => setShowModal(false)}>✕</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group">
                  <label className="form-label">Maintenance Request</label>
                  <select className="form-select" required value={form.requestId} onChange={e => setForm({ ...form, requestId: e.target.value })}>
                    <option value="">Select completed request…</option>
                    {eligibleRequests.map(r => (
                      <option key={r.id} value={r.id}>#{r.id} — {r.branch?.name} ({r.priority})</option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Work Summary</label>
                  <textarea className="form-textarea" required value={form.workSummary} onChange={e => setForm({ ...form, workSummary: e.target.value })} placeholder="Describe the work completed, parts replaced, and outcome…" style={{ minHeight: 120 }} />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Submitting…' : 'Submit Report'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
