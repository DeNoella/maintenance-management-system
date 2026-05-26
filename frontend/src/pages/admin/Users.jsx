import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { userAPI, branchAPI } from '../../services/api';
import toast from 'react-hot-toast';

const ROLES = ['ADMIN', 'BRANCH_MANAGER', 'TECHNICIAN'];

export default function AdminUsers() {
  const [users, setUsers] = useState([]);
  const [branches, setBranches] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [form, setForm] = useState({ username: '', fullName: '', phone: '', role: 'TECHNICIAN', branchId: '', password: '' });
  const [saving, setSaving] = useState(false);

  const load = () => {
    Promise.all([userAPI.getAll(), branchAPI.getAll()])
      .then(([u, b]) => { setUsers(u.data || []); setBranches(b.data || []); })
      .catch(() => toast.error('Failed to load users'))
      .finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      await userAPI.create({ ...form, branchId: form.branchId || null });
      toast.success('User created');
      setShowModal(false);
      setForm({ username: '', fullName: '', phone: '', role: 'TECHNICIAN', branchId: '', password: '' });
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to create user');
    } finally { setSaving(false); }
  };

  const handleDeactivate = async (id, name) => {
    if (!confirm(`Deactivate ${name}?`)) return;
    try {
      await userAPI.deactivate(id);
      toast.success('User deactivated');
      load();
    } catch { toast.error('Failed to deactivate'); }
  };

  const roleBadge = (r) => {
    const m = { ADMIN: 'badge-red', BRANCH_MANAGER: 'badge-blue', TECHNICIAN: 'badge-green' };
    return <span className={`badge ${m[r] || 'badge-gray'}`}>{r?.replace('_', ' ')}</span>;
  };

  return (
    <AppLayout pageTitle="User Management">
      <div className="card-header" style={{ marginBottom: '1.25rem' }}>
        <h2>Users</h2>
        <button className="btn btn-primary btn-sm" onClick={() => setShowModal(true)}>+ New User</button>
      </div>

      {loading ? <div className="loading"><div className="spinner" /></div> : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Username</th>
                <th>Role</th>
                <th>Branch</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map(u => (
                <tr key={u.id}>
                  <td style={{ fontWeight: 500 }}>{u.fullName}</td>
                  <td className="text-mono" style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>@{u.username}</td>
                  <td>{roleBadge(u.role)}</td>
                  <td style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>{u.branch?.name || '—'}</td>
                  <td>
                    <span className={`badge ${u.isActive ? 'badge-green' : 'badge-gray'}`}>
                      {u.isActive ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td>
                    {u.isActive && (
                      <button className="btn btn-danger btn-sm" onClick={() => handleDeactivate(u.id, u.fullName)}>
                        Deactivate
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={(e) => { if (e.target === e.currentTarget) setShowModal(false); }}>
          <div className="modal">
            <div className="modal-header">
              <h3>Create New User</h3>
              <button className="btn btn-secondary btn-sm" onClick={() => setShowModal(false)}>✕</button>
            </div>
            <form onSubmit={handleCreate}>
              <div className="modal-body">
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">Full Name</label>
                    <input className="form-input" required value={form.fullName} onChange={e => setForm({ ...form, fullName: e.target.value })} placeholder="Jane Doe" />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Username</label>
                    <input className="form-input" required value={form.username} onChange={e => setForm({ ...form, username: e.target.value })} placeholder="jane.doe" />
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">Phone</label>
                    <input className="form-input" value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} placeholder="+250 78..." />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Role</label>
                    <select className="form-select" value={form.role} onChange={e => setForm({ ...form, role: e.target.value })}>
                      {ROLES.map(r => <option key={r} value={r}>{r.replace('_', ' ')}</option>)}
                    </select>
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">Branch</label>
                    <select className="form-select" value={form.branchId} onChange={e => setForm({ ...form, branchId: e.target.value })}>
                      <option value="">— No branch —</option>
                      {branches.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label className="form-label">Password</label>
                    <input className="form-input" type="password" required value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} placeholder="••••••••" />
                  </div>
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary" disabled={saving}>{saving ? 'Creating…' : 'Create User'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
