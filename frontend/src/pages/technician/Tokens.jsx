import { useEffect, useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { tokenAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import { QRCodeSVG } from 'qrcode.react';
import toast from 'react-hot-toast';

function Countdown({ expiresAt }) {
  const [remaining, setRemaining] = useState('');

  useEffect(() => {
    const update = () => {
      const diff = new Date(expiresAt) - new Date();
      if (diff <= 0) { setRemaining('Expired'); return; }
      const d = Math.floor(diff / 86400000);
      const h = Math.floor((diff % 86400000) / 3600000);
      const m = Math.floor((diff % 3600000) / 60000);
      setRemaining(d > 0 ? `${d}d ${h}h ${m}m` : `${h}h ${m}m`);
    };
    update();
    const t = setInterval(update, 60000);
    return () => clearInterval(t);
  }, [expiresAt]);

  return <span className="token-countdown">{remaining}</span>;
}

export default function TechnicianTokens() {
  const { user } = useAuth();
  const [tokens, setTokens] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selected, setSelected] = useState(null);

  useEffect(() => {
    tokenAPI.getByTechnician(user?.id)
      .then(r => setTokens(r.data || []))
      .catch(() => toast.error('Failed to load tokens'))
      .finally(() => setLoading(false));
  }, []);

  const isActive = (t) => t.status === 'ACTIVE' && new Date(t.expiresAt) > new Date();

  return (
    <AppLayout pageTitle="My Tokens">
      <h2 style={{ marginBottom: '1.25rem' }}>Access Tokens</h2>

      {loading ? <div className="loading"><div className="spinner" /></div> : (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: 12 }}>
          {tokens.map(t => (
            <div
              key={t.id}
              className="card"
              style={{ cursor: isActive(t) ? 'pointer' : 'default', opacity: isActive(t) ? 1 : 0.7, transition: 'box-shadow 0.15s' }}
              onClick={() => isActive(t) && setSelected(t)}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.75rem' }}>
                <div>
                  <div style={{ fontWeight: 500, marginBottom: 2 }}>{t.branch?.name || '—'}</div>
                  <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', fontFamily: 'var(--font-mono)' }}>
                    {t.tokenCode?.slice(0, 16)}…
                  </div>
                </div>
                <span className={`badge ${isActive(t) ? 'badge-green' : 'badge-gray'}`}>
                  {isActive(t) ? 'Active' : t.status}
                </span>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.5rem', fontSize: '0.82rem', color: 'var(--text-secondary)' }}>
                <div><span style={{ color: 'var(--text-muted)', fontSize: '0.72rem', textTransform: 'uppercase', letterSpacing: '0.04em', display: 'block' }}>Valid From</span>{t.validFrom ? new Date(t.validFrom).toLocaleDateString() : '—'}</div>
                <div><span style={{ color: 'var(--text-muted)', fontSize: '0.72rem', textTransform: 'uppercase', letterSpacing: '0.04em', display: 'block' }}>Expires</span>{t.expiresAt ? new Date(t.expiresAt).toLocaleDateString() : '—'}</div>
              </div>

              {isActive(t) && (
                <div style={{ marginTop: '0.75rem', paddingTop: '0.75rem', borderTop: '1px solid var(--border)', color: 'var(--accent-text)', fontSize: '0.8rem', fontWeight: 500 }}>
                  Tap to show QR code →
                </div>
              )}
            </div>
          ))}
          {tokens.length === 0 && <div className="empty-state"><p>No tokens issued yet. Submit a maintenance request to receive a token.</p></div>}
        </div>
      )}

      {/* QR Modal */}
      {selected && (
        <div className="modal-overlay" onClick={() => setSelected(null)}>
          <div className="modal" style={{ maxWidth: 380 }} onClick={e => e.stopPropagation()}>
            <div className="modal-header">
              <h3>Access Token</h3>
              <button className="btn btn-secondary btn-sm" onClick={() => setSelected(null)}>✕</button>
            </div>
            <div className="token-card">
              <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)', marginBottom: '1rem' }}>
                {selected.branch?.name}
              </div>
              <div style={{ padding: '1rem', background: 'white', borderRadius: 8, display: 'inline-block', margin: '0 auto' }}>
                <QRCodeSVG value={selected.qrData || selected.tokenCode} size={200} />
              </div>
              <div className="token-code">{selected.tokenCode}</div>
              <div style={{ marginTop: '0.75rem', fontSize: '0.78rem', color: 'var(--text-muted)' }}>Time remaining</div>
              <Countdown expiresAt={selected.expiresAt} />
              <p style={{ fontSize: '0.78rem', color: 'var(--text-muted)', marginTop: '0.75rem' }}>
                Present this QR code to the Branch Manager for entry
              </p>
            </div>
          </div>
        </div>
      )}
    </AppLayout>
  );
}
