import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { requestAPI, branchAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';

const PRIORITIES = ['LOW', 'MEDIUM', 'HIGH', 'URGENT'];

export default function TechnicianRequests() {
  const { user } = useAuth();
  const [requests, setRequests] = useState([]);
  const [branches, setBranches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [form, setForm] = useState({ branchId: '', issueDescription: '', priority: 'MEDIUM', preferredVisitDate: '' });
  const [saving, setSaving] = useState(false);

  const load = () => {
    Promise.all([
      requestAPI.getByTechnician(user?.id),
      branchAPI.getAll(),
    ]).then(([r, b]) => {
      setRequests(r.data || []);
      setBranches(b.data || []);
    }).catch(() => toast.error('Failed to load'))
    .finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await requestAPI.submit({ ...form, technicianId: user?.id });
      toast.success('Request submitted');
      setShowModal(false);
      setForm({ branchId: '', issueDescription: '', priority: 'MEDIUM', preferredVisitDate: '' });
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to submit');
    } finally { setSaving(false); }
  };

  const statusBadge = (s) => {
    const m = { PENDING: 'badge-amber', APPROVED: 'badge-green', REJECTED: 'badge-red', IN_PROGRESS: 'badge-blue', COMPLETED: 'badge-gray' };
    return <span className={`badge ${m[s] || 'badge-gray'}`}>{s}</span>;
  };

  return (
    <AppLayout pageTitle="My Requests">
      <div className="card-header" style={{ marginBottom: '1.25rem' }}>
        <h2>Maintenance Requests</h2>
        <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>+ New Request</button>
      </div>

      {loading ? <div className="loading"><div className="spinner" /></div> : (
        <div className="table-wrap">
          <table>
            <thead><tr><th>Branch</th><th>Issue</th><th>Priority</th><th>Status</th><th>Preferred Date</th><th>Submitted</th></tr></thead>
            <tbody>
              {requests.map(r => (
                <tr key={r.id}>
                  <td style={{ fontWeight: 500 }}>{r.branch?.name || '—'}</td>
                  <td style={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap', color: 'var(--text-secondary)', fontSize: '0.875rem' }}>{r.issueDescription}</td>
                  <td><span className={`priority-${r.priority}`} style={{ fontSize: '0.8rem', fontWeight: 500 }}>{r.priority}</span></td>
                  <td>{statusBadge(r.status)}</td>
                  <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{r.preferredVisitDate || '—'}</td>
                  <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{r.submittedAt ? new Date(r.submittedAt).toLocaleDateString() : '—'}</td>
                </tr>
              ))}
              {requests.length === 0 && <tr><td colSpan={6} style={{ textAlign: 'center', color: 'var(--text-muted)', padding: '2rem' }}>No requests yet</td></tr>}
            </tbody>
          </table>
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={e => { if (e.target === e.currentTarget) setShowModal(false); }}>
          <div className="modal">
            <div className="modal-header">
              <h3>Submit Maintenance Request</h3>
              <button className="btn btn-secondary btn-sm" onClick={() => setShowModal(false)}>✕</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group">
                  <label className="form-label">Branch Location</label>
                  <select className="form-select" required value={form.branchId} onChange={e => setForm({ ...form, branchId: e.target.value })}>
                    <option value="">Select branch…</option>
                    {branches.filter(b => b.isActive).map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Issue Description</label>
                  <textarea className="form-textarea" required value={form.issueDescription} onChange={e => setForm({ ...form, issueDescription: e.target.value })} placeholder="Describe the maintenance issue in detail…" />
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">Priority</label>
                    <select className="form-select" value={form.priority} onChange={e => setForm({ ...form, priority: e.target.value })}>
                      {PRIORITIES.map(p => <option key={p} value={p}>{p}</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label className="form-label">Preferred Visit Date</label>
                    <input className="form-input" type="date" value={form.preferredVisitDate} onChange={e => setForm({ ...form, preferredVisitDate: e.target.value })} min={new Date().toISOString().split('T')[0]} />
                  </div>
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Submitting…' : 'Submit Request'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
