import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';

const Icon = ({ d, size = 16 }) => (
  <svg width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
    <path d={d} />
  </svg>
);

const ICONS = {
  dashboard: "M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z",
  users: "M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8zM23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75",
  building: "M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z M9 22V12h6v10",
  branch: "M6 3v12 M18 9a3 3 0 1 0 0-6 3 3 0 0 0 0 6z M6 21a3 3 0 1 0 0-6 3 3 0 0 0 0 6z M15 6H9a3 3 0 0 0-3 3v6",
  wrench: "M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z",
  token: "M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z",
  report: "M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z M14 2v6h6 M16 13H8 M16 17H8 M10 9H8",
  activity: "M22 12h-4l-3 9L9 3l-3 9H2",
  logout: "M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4 M16 17l5-5-5-5 M21 12H9",
  check: "M9 11l3 3L22 4 M21 12v7a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11",
  service: "M12 2L2 7l10 5 10-5-10-5z M2 17l10 5 10-5 M2 12l10 5 10-5",
};

const navConfig = {
  ADMIN: [
    { section: 'Overview', links: [
      { to: '/admin/dashboard', label: 'Dashboard', icon: 'dashboard' },
      { to: '/admin/activity', label: 'Activity Log', icon: 'activity' },
      { to: '/admin/reports', label: 'System Reports', icon: 'report' },
    ]},
    { section: 'Management', links: [
      { to: '/admin/users', label: 'Users', icon: 'users' },
      { to: '/admin/companies', label: 'Companies', icon: 'building' },
      { to: '/admin/branches', label: 'Branches', icon: 'branch' },
      { to: '/admin/services', label: 'Services', icon: 'service' },
    ]},
  ],
  BRANCH_MANAGER: [
    { section: 'Overview', links: [
      { to: '/manager/dashboard', label: 'Dashboard', icon: 'dashboard' },
      { to: '/manager/activity', label: 'Activity Log', icon: 'activity' },
    ]},
    { section: 'Operations', links: [
      { to: '/manager/requests', label: 'Maintenance Requests', icon: 'wrench' },
      { to: '/manager/tokens', label: 'Verify Tokens', icon: 'token' },
      { to: '/manager/technicians', label: 'Technicians', icon: 'users' },
      { to: '/manager/reports', label: 'Branch Reports', icon: 'report' },
      { to: '/manager/completions', label: 'Completion Reports', icon: 'check' },
    ]},
  ],
  TECHNICIAN: [
    { section: 'Overview', links: [
      { to: '/technician/dashboard', label: 'Dashboard', icon: 'dashboard' },
      { to: '/technician/activity', label: 'Activity Log', icon: 'activity' },
    ]},
    { section: 'Work', links: [
      { to: '/technician/requests', label: 'My Requests', icon: 'wrench' },
      { to: '/technician/tokens', label: 'My Tokens', icon: 'token' },
      { to: '/technician/completions', label: 'Completion Reports', icon: 'report' },
    ]},
  ],
};

export default function AppLayout({ children, pageTitle }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    toast.success('Signed out');
    navigate('/login');
  };

  const roleLabel = { ADMIN: 'Administrator', BRANCH_MANAGER: 'Branch Manager', TECHNICIAN: 'Technician' };
  const sections = navConfig[user?.role] || [];

  return (
    <div className="app-layout">
      {/* Sidebar */}
      <aside className="sidebar">
        <div className="sidebar-brand">
          <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
            <div style={{ width: 28, height: 28, background: 'rgba(255,255,255,0.12)', borderRadius: 6, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2"><rect x="3" y="11" width="18" height="11" rx="2"/><path d="M7 11V7a5 5 0 0 1 10 0v4"/></svg>
            </div>
            <div>
              <div className="sidebar-brand-name">MMS</div>
              <div className="sidebar-brand-role">{roleLabel[user?.role]}</div>
            </div>
          </div>
        </div>

        <nav className="sidebar-nav">
          {sections.map(({ section, links }) => (
            <div key={section}>
              <div className="nav-section-label">{section}</div>
              {links.map(({ to, label, icon }) => (
                <NavLink
                  key={to}
                  to={to}
                  className={({ isActive }) => `nav-link${isActive ? ' active' : ''}`}
                >
                  <Icon d={ICONS[icon]} />
                  {label}
                </NavLink>
              ))}
            </div>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div style={{ marginBottom: '0.5rem' }}>
            <div style={{ fontSize: '0.85rem', fontWeight: 500, color: 'rgba(255,255,255,0.85)' }}>{user?.fullName}</div>
            <div style={{ fontSize: '0.72rem', color: 'rgba(255,255,255,0.4)', fontFamily: 'var(--font-mono)' }}>@{user?.username}</div>
          </div>
          <button className="nav-link" style={{ color: 'rgba(255,255,255,0.5)' }} onClick={handleLogout}>
            <Icon d={ICONS.logout} />
            Sign out
          </button>
        </div>
      </aside>

      {/* Main */}
      <div className="main-content">
        <header className="topbar">
          <span className="topbar-title">{pageTitle}</span>
          <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', fontFamily: 'var(--font-mono)' }}>
            {new Date().toLocaleDateString('en-GB', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
          </div>
        </header>
        <main className="page-content">{children}</main>
      </div>
    </div>
  );
}
