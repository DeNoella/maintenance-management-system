import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import toast from "react-hot-toast";

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: "", password: "" });
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const userData = await login(form.username, form.password);
      toast.success(`Welcome, ${userData.fullName}`);

      // System engineer first login → redirect to setup wizard
      if (userData.role === "SYSTEM_ENGINEER" || userData.mustChangePassword) {
        navigate("/setup");
        return;
      }

      if (userData.role === "ADMIN") navigate("/admin/dashboard");
      else if (userData.role === "BRANCH_MANAGER")
        navigate("/manager/dashboard");
      else navigate("/technician/dashboard");
    } catch (err) {
      toast.error(err.response?.data?.message || "Invalid credentials");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        background: "linear-gradient(135deg, var(--accent) 0%, #403A82 100%)",
      }}
    >
      {/* Left brand panel */}
      <div
        style={{
          flex: "0 0 420px",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          padding: "3rem",
          color: "#fff",
        }}
        className="login-brand-panel"
      >
        <div style={{ marginBottom: "2.5rem" }}>
          <div
            style={{
              width: 52,
              height: 52,
              background: "rgba(255,255,255,0.15)",
              borderRadius: 12,
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              marginBottom: "1.5rem",
            }}
          >
            <svg
              width="26"
              height="26"
              viewBox="0 0 24 24"
              fill="none"
              stroke="white"
              strokeWidth="2"
            >
              <rect x="3" y="11" width="18" height="11" rx="2" />
              <path d="M7 11V7a5 5 0 0 1 10 0v4" />
            </svg>
          </div>
          <h1
            style={{
              fontSize: "1.6rem",
              fontWeight: 600,
              marginBottom: "0.5rem",
              letterSpacing: "-0.02em",
            }}
          >
            Maintenance Access System
          </h1>
          <p
            style={{
              color: "rgba(255,255,255,0.6)",
              fontSize: "0.9rem",
              lineHeight: 1.6,
            }}
          >
            Secure, role-based access management for maintenance operations
            across all branches.
          </p>
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
          {[
            ["Admin", "Manage companies, branches & users"],
            ["Branch Manager", "Verify tokens & approve requests"],
            ["Technician", "Request access & submit reports"],
          ].map(([role, desc]) => (
            <div
              key={role}
              style={{ display: "flex", alignItems: "flex-start", gap: 10 }}
            >
              <div
                style={{
                  width: 6,
                  height: 6,
                  borderRadius: "50%",
                  background: "rgba(255,255,255,0.5)",
                  marginTop: 7,
                  flexShrink: 0,
                }}
              />
              <div>
                <div style={{ fontSize: "0.85rem", fontWeight: 500 }}>
                  {role}
                </div>
                <div
                  style={{
                    fontSize: "0.78rem",
                    color: "rgba(255,255,255,0.5)",
                  }}
                >
                  {desc}
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Right login form */}
      <div
        style={{
          flex: 1,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          background: "var(--bg)",
          padding: "2rem",
        }}
      >
        <div style={{ width: "100%", maxWidth: 380 }}>
          <div style={{ marginBottom: "2rem" }}>
            <h2 style={{ marginBottom: "4px" }}>Sign in</h2>
            <p style={{ color: "var(--text-muted)", fontSize: "0.85rem" }}>
              Access restricted to authorized personnel
            </p>
          </div>

          <div className="card">
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label className="form-label">Username</label>
                <input
                  className="form-input"
                  type="text"
                  placeholder="Enter your username"
                  value={form.username}
                  onChange={(e) =>
                    setForm({ ...form, username: e.target.value })
                  }
                  required
                  autoFocus
                />
              </div>
              <div className="form-group">
                <label className="form-label">Password</label>
                <input
                  className="form-input"
                  type="password"
                  placeholder="Enter your password"
                  value={form.password}
                  onChange={(e) =>
                    setForm({ ...form, password: e.target.value })
                  }
                  required
                />
              </div>
              <button
                type="submit"
                className="btn btn-primary"
                style={{
                  width: "100%",
                  justifyContent: "center",
                  marginTop: "0.5rem",
                }}
                disabled={loading}
              >
                {loading ? (
                  <>
                    <span
                      className="spinner"
                      style={{ width: 14, height: 14, borderWidth: 2 }}
                    />{" "}
                    Signing in…
                  </>
                ) : (
                  "Sign in →"
                )}
              </button>
            </form>
          </div>

          <p
            style={{
              textAlign: "center",
              marginTop: "1.25rem",
              fontSize: "0.72rem",
              color: "var(--text-muted)",
              fontFamily: "var(--font-mono)",
            }}
          >
            MMS v1.0 · First time? Use the credentials provided by your system
            engineer.
          </p>
        </div>
      </div>

      {/* Hide brand panel on small screens */}
      <style>{`
        @media (max-width: 768px) { .login-brand-panel { display: none !important; } }
      `}</style>
    </div>
  );
}
