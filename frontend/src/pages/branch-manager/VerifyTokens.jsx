import { useState } from 'react';
import AppLayout from '../../components/layout/AppLayout';
import { tokenAPI } from '../../services/api';
import { useAuth } from '../../context/AuthContext';
import toast from 'react-hot-toast';

export default function VerifyTokens() {
  const { user } = useAuth();
  const [tokenCode, setTokenCode] = useState('');
  const [result, setResult] = useState(null);
  const [searching, setSearching] = useState(false);
  const [deciding, setDeciding] = useState(false);
  const [notes, setNotes] = useState('');

  const handleVerify = async (e) => {
    e.preventDefault();
    if (!tokenCode.trim()) return;
    setSearching(true);
    setResult(null);
    try {
      const res = await tokenAPI.verify(tokenCode.trim());
      setResult(res.data);
    } catch (err) {
      const status = err.response?.status;
      if (status === 404) toast.error('Token not found');
      else toast.error('Verification failed');
    } finally { setSearching(false); }
  };

  const handleDecision = async (decision) => {
    if (!result) return;
    setDeciding(true);
    try {
      await tokenAPI.decide(result.id, decision, notes);
      toast.success(decision === 'GRANTED' ? 'Access granted ✓' : 'Access denied');
      setResult({ ...result, _decided: decision });
    } catch { toast.error('Failed to record decision'); }
    finally { setDeciding(false); }
  };

  const isExpired = result && result.status === 'EXPIRED';
  const isActive = result && result.status === 'ACTIVE';

  return (
    <AppLayout pageTitle="Token Verification">
      <div style={{ maxWidth: 560 }}>
        <div className="card" style={{ marginBottom: '1.5rem' }}>
          <h2 style={{ marginBottom: '1rem' }}>Scan or Enter Token</h2>
          <form onSubmit={handleVerify} style={{ display: 'flex', gap: 10 }}>
            <input
              className="form-input"
              placeholder="Enter token code or scan QR…"
              value={tokenCode}
              onChange={e => setTokenCode(e.target.value)}
              style={{ flex: 1, fontFamily: 'var(--font-mono)', fontSize: '0.85rem' }}
            />
            <button type="submit" className="btn btn-primary" disabled={searching}>
              {searching ? <span className="spinner" style={{ width: 14, height: 14 }} /> : 'Verify'}
            </button>
          </form>
        </div>

        {result && (
          <div className="card">
            {/* Status banner */}
            <div style={{
              padding: '10px 14px', borderRadius: 6, marginBottom: '1.25rem',
              background: isActive ? 'var(--accent-light)' : isExpired ? 'var(--red-light)' : 'var(--amber-light)',
              color: isActive ? 'var(--accent-text)' : isExpired ? 'var(--red)' : 'var(--amber)',
              fontWeight: 500, fontSize: '0.9rem', display: 'flex', alignItems: 'center', gap: 8
            }}>
              <span style={{ fontSize: '1.1rem' }}>{isActive ? '✓' : isExpired ? '✕' : '⚠'}</span>
              {isActive ? 'Token is valid and active' : isExpired ? 'Token has expired' : `Token status: ${result.status}`}
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '0.75rem 1.5rem', marginBottom: '1rem' }}>
              {[
                ['Technician', result.technician?.fullName || '—'],
                ['Branch', result.branch?.name || '—'],
                ['Valid From', result.validFrom ? new Date(result.validFrom).toLocaleDateString() : '—'],
                ['Expires At', result.expiresAt ? new Date(result.expiresAt).toLocaleString() : '—'],
              ].map(([label, value]) => (
                <div key={label}>
                  <div style={{ fontSize: '0.72rem', color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.04em', marginBottom: 2 }}>{label}</div>
                  <div style={{ fontWeight: 500 }}>{value}</div>
                </div>
              ))}
            </div>

            <hr />

            {result._decided ? (
              <div style={{ textAlign: 'center', padding: '1rem', color: result._decided === 'GRANTED' ? 'var(--accent-text)' : 'var(--red)', fontWeight: 500 }}>
                Decision recorded: Access {result._decided === 'GRANTED' ? 'Granted ✓' : 'Denied ✕'}
              </div>
            ) : isActive ? (
              <div style={{ marginTop: '1rem' }}>
                <div className="form-group">
                  <label className="form-label">Notes (optional)</label>
                  <textarea className="form-textarea" value={notes} onChange={e => setNotes(e.target.value)} placeholder="Any remarks about this visit…" style={{ minHeight: 60 }} />
                </div>
                <div style={{ display: 'flex', gap: 10, marginTop: '0.5rem' }}>
                  <button className="btn btn-primary" style={{ flex: 1, justifyContent: 'center' }} onClick={() => handleDecision('GRANTED')} disabled={deciding}>
                    ✓ Grant Access
                  </button>
                  <button className="btn btn-danger" style={{ flex: 1, justifyContent: 'center' }} onClick={() => handleDecision('DENIED')} disabled={deciding}>
                    ✕ Deny Access
                  </button>
                </div>
              </div>
            ) : (
              <p style={{ color: 'var(--text-muted)', fontSize: '0.875rem', marginTop: '0.5rem' }}>
                This token cannot be used for entry.
              </p>
            )}
          </div>
        )}
      </div>
    </AppLayout>
  );
}
