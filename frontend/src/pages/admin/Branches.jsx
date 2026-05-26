import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { branchAPI, companyAPI } from '../../services/api';
import toast from 'react-hot-toast';

export default function AdminBranches() {
  const [branches, setBranches] = useState([]);
  const [companies, setCompanies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [form, setForm] = useState({ name: '', address: '', companyId: '' });
  const [saving, setSaving] = useState(false);

  const load = () => {
    Promise.all([branchAPI.getAll(), companyAPI.getAll()])
      .then(([b, c]) => { setBranches(b.data || []); setCompanies(c.data || []); })
      .catch(() => toast.error('Failed to load'))
      .finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await branchAPI.create(form);
      toast.success('Branch created');
      setShowModal(false);
      setForm({ name: '', address: '', companyId: '' });
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed');
    } finally { setSaving(false); }
  };

  const handleDeactivate = async (id, name) => {
    if (!confirm(`Deactivate branch "${name}"?`)) return;
    try { await branchAPI.deactivate(id); toast.success('Deactivated'); load(); }
    catch { toast.error('Failed'); }
  };

  return (
    <AppLayout pageTitle="Branch Management">
      <div className="card-header" style={{ marginBottom: '1.25rem' }}>
        <h2>Branches</h2>
        <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>+ New Branch</button>
      </div>

      {loading ? <div className="loading"><div className="spinner" /></div> : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr><th>Name</th><th>Company</th><th>Address</th><th>Status</th><th>Actions</th></tr>
            </thead>
            <tbody>
              {branches.map(b => (
                <tr key={b.id}>
                  <td style={{ fontWeight: 500 }}>{b.name}</td>
                  <td style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>{b.company?.name || '—'}</td>
                  <td style={{ color: 'var(--text-muted)', fontSize: '0.85rem', maxWidth: 200 }}>{b.address || '—'}</td>
                  <td><span className={`badge ${b.isActive ? 'badge-green' : 'badge-gray'}`}>{b.isActive ? 'Active' : 'Inactive'}</span></td>
                  <td>
                    {b.isActive && <button className="btn btn-danger btn-sm" onClick={() => handleDeactivate(b.id, b.name)}>Deactivate</button>}
                  </td>
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
              <h3>Create Branch</h3>
              <button className="btn btn-secondary btn-sm" onClick={() => setShowModal(false)}>✕</button>
            </div>
            <form onSubmit={handleCreate}>
              <div className="modal-body">
                <div className="form-group">
                  <label className="form-label">Branch Name</label>
                  <input className="form-input" required value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} placeholder="Kigali Central Branch" />
                </div>
                <div className="form-group">
                  <label className="form-label">Company</label>
                  <select className="form-select" required value={form.companyId} onChange={e => setForm({ ...form, companyId: e.target.value })}>
                    <option value="">Select company…</option>
                    {companies.filter(c => c.isActive).map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Address</label>
                  <textarea className="form-textarea" value={form.address} onChange={e => setForm({ ...form, address: e.target.value })} placeholder="KG 123 St, Kigali" />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Creating…' : 'Create Branch'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
