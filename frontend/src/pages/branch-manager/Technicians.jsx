import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { userAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';

export default function ManagerTechnicians() {
  const { user } = useAuth();
  const [techs, setTechs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [form, setForm] = useState({ username: '', fullName: '', phone: '', password: '', skills: '' });
  const [saving, setSaving] = useState(false);

  const load = () => {
    userAPI.getByBranch(user?.branch?.id)
      .then(r => setTechs((r.data || []).filter(u => u.role === 'TECHNICIAN')))
      .catch(() => toast.error('Failed to load'))
      .finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const skills = form.skills.split(',').map(s => s.trim()).filter(Boolean);
      await userAPI.createTechnician({
        username: form.username, fullName: form.fullName, phone: form.phone,
        rawPassword: form.password, branchId: user?.branch?.id, skills,
        managerId: user?.id
      });
      toast.success('Technician created');
      setShowModal(false);
      setForm({ username: '', fullName: '', phone: '', password: '', skills: '' });
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed');
    } finally { setSaving(false); }
  };

  return (
    <AppLayout pageTitle="Technicians">
      <div className="card-header" style={{ marginBottom: '1.25rem' }}>
        <h2>Branch Technicians</h2>
        <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>+ Add Technician</button>
      </div>

      {loading ? <div className="loading"><div className="spinner" /></div> : (
        <div className="table-wrap">
          <table>
            <thead><tr><th>Name</th><th>Username</th><th>Phone</th><th>Status</th></tr></thead>
            <tbody>
              {techs.map(t => (
                <tr key={t.id}>
                  <td style={{ fontWeight: 500 }}>{t.fullName}</td>
                  <td className="text-mono" style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>@{t.username}</td>
                  <td>{t.phone || '—'}</td>
                  <td><span className={`badge ${t.isActive ? 'badge-green' : 'badge-gray'}`}>{t.isActive ? 'Active' : 'Inactive'}</span></td>
                </tr>
              ))}
              {techs.length === 0 && <tr><td colSpan={4} style={{ textAlign: 'center', color: 'var(--text-muted)', padding: '2rem' }}>No technicians found</td></tr>}
            </tbody>
          </table>
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={e => { if (e.target === e.currentTarget) setShowModal(false); }}>
          <div className="modal">
            <div className="modal-header">
              <h3>Add Technician</h3>
              <button className="btn btn-secondary btn-sm" onClick={() => setShowModal(false)}>✕</button>
            </div>
            <form onSubmit={handleCreate}>
              <div className="modal-body">
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">Full Name</label>
                    <input className="form-input" required value={form.fullName} onChange={e => setForm({ ...form, fullName: e.target.value })} placeholder="Jean Baptiste" />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Username</label>
                    <input className="form-input" required value={form.username} onChange={e => setForm({ ...form, username: e.target.value })} placeholder="jean.b" />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">Phone</label>
                    <input className="form-input" value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} placeholder="+250 78..." />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Password</label>
                    <input className="form-input" type="password" required value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} placeholder="••••••••" />
                  </div>
                </div>
                <div className="form-group">
                  <label className="form-label">Skills (comma-separated)</label>
                  <input className="form-input" value={form.skills} onChange={e => setForm({ ...form, skills: e.target.value })} placeholder="Electrical, Plumbing, HVAC" />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Creating…' : 'Create Technician'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
