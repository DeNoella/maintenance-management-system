import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { companyAPI, serviceAPI } from '../../services/api';
import toast from 'react-hot-toast';

export default function AdminCompanies() {
  const [companies, setCompanies] = useState([]);
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [form, setForm] = useState({ name: '', robCertificate: '', phone: '', address: '', serviceIds: [] });
  const [saving, setSaving] = useState(false);

  const load = () => {
    Promise.all([companyAPI.getAll(), serviceAPI.getAll()])
      .then(([c, s]) => { setCompanies(c.data || []); setServices(s.data || []); })
      .catch(() => toast.error('Failed to load'))
      .finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, []);

  const toggleService = (id) => {
    const ids = form.serviceIds;
    setForm({ ...form, serviceIds: ids.includes(id) ? ids.filter(i => i !== id) : [...ids, id] });
  };

  const handleCreate = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await companyAPI.create(form);
      toast.success('Company created');
      setShowModal(false);
      setForm({ name: '', robCertificate: '', phone: '', address: '', serviceIds: [] });
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed');
    } finally { setSaving(false); }
  };

  const handleDeactivate = async (id, name) => {
    if (!confirm(`Deactivate "${name}"?`)) return;
    try { await companyAPI.deactivate(id); toast.success('Deactivated'); load(); }
    catch { toast.error('Failed'); }
  };

  return (
    <AppLayout pageTitle="Company Management">
      <div className="card-header" style={{ marginBottom: '1.25rem' }}>
        <h2>Companies</h2>
        <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>+ New Company</button>
      </div>

      {loading ? <div className="loading"><div className="spinner" /></div> : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr><th>Name</th><th>RDB Certificate</th><th>Phone</th><th>Status</th><th>Actions</th></tr>
            </thead>
            <tbody>
              {companies.map(c => (
                <tr key={c.id}>
                  <td style={{ fontWeight: 500 }}>{c.name}</td>
                  <td className="text-mono" style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>{c.robCertificate || '—'}</td>
                  <td>{c.phone || '—'}</td>
                  <td><span className={`badge ${c.isActive ? 'badge-green' : 'badge-gray'}`}>{c.isActive ? 'Active' : 'Inactive'}</span></td>
                  <td>
                    {c.isActive && <button className="btn btn-danger btn-sm" onClick={() => handleDeactivate(c.id, c.name)}>Deactivate</button>}
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
              <h3>Create Company</h3>
              <button className="btn btn-secondary btn-sm" onClick={() => setShowModal(false)}>✕</button>
            </div>
            <form onSubmit={handleCreate}>
              <div className="modal-body">
                <div className="form-group">
                  <label className="form-label">Company Name</label>
                  <input className="form-input" required value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} placeholder="Acme Maintenance Ltd" />
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">RDB Certificate</label>
                    <input className="form-input" value={form.robCertificate} onChange={e => setForm({ ...form, robCertificate: e.target.value })} placeholder="RDB/2024/..." />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Phone</label>
                    <input className="form-input" value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} placeholder="+250 78..." />
                  </div>
                </div>
                <div className="form-group">
                  <label className="form-label">Address</label>
                  <textarea className="form-textarea" value={form.address} onChange={e => setForm({ ...form, address: e.target.value })} placeholder="KG 123 St, Kigali" />
                </div>
                {services.length > 0 && (
                  <div className="form-group">
                    <label className="form-label">Services Offered</label>
                    <div style={{ display: 'flex', flexWrap: 'wrap', gap: 8, marginTop: 4 }}>
                      {services.map(s => (
                        <label key={s.id} style={{ display: 'flex', alignItems: 'center', gap: 5, cursor: 'pointer', fontSize: '0.875rem' }}>
                          <input type="checkbox" checked={form.serviceIds.includes(s.id)} onChange={() => toggleService(s.id)} />
                          {s.name}
                        </label>
                      ))}
                    </div>
                  </div>
                )}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Creating…' : 'Create'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
