import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { Toaster } from "react-hot-toast";
import { AuthProvider, useAuth } from "./context/AuthContext";
import ProtectedRoute from "./components/ui/ProtectedRoute";

// Pages
import Login from "./pages/Login";
import SystemSetup from "./pages/SystemSetup";
import ActivityLog from "./pages/ActivityLog";

// Admin
import AdminDashboard from "./pages/admin/Dashboard";
import AdminUsers from "./pages/admin/Users";
import AdminCompanies from "./pages/admin/Companies";
import AdminBranches from "./pages/admin/Branches";
import AdminServices from "./pages/admin/Services";

// Branch Manager
import ManagerDashboard from "./pages/branch-manager/Dashboard";
import VerifyTokens from "./pages/branch-manager/VerifyTokens";
import ManagerTechnicians from "./pages/branch-manager/Technicians";
import ManagerCompletions from "./pages/branch-manager/Completions";

// Technician
import TechnicianDashboard from "./pages/technician/Dashboard";
import TechnicianRequests from "./pages/technician/Requests";
import TechnicianTokens from "./pages/technician/Tokens";
import TechnicianCompletions from "./pages/technician/Completions";

function RootRedirect() {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (user.role === "SYSTEM_ENGINEER" || user.mustChangePassword)
    return <Navigate to="/setup" replace />;
  if (user.role === "ADMIN") return <Navigate to="/admin/dashboard" replace />;
  if (user.role === "BRANCH_MANAGER")
    return <Navigate to="/manager/dashboard" replace />;
  return <Navigate to="/technician/dashboard" replace />;
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Toaster
          position="top-right"
          toastOptions={{
            duration: 3500,
            style: { fontFamily: "'Sora', sans-serif", fontSize: "0.875rem" },
          }}
        />
        <Routes>
          <Route path="/" element={<RootRedirect />} />
          <Route path="/login" element={<Login />} />
          <Route
            path="/setup"
            element={
              <ProtectedRoute allowedRoles={["SYSTEM_ENGINEER", "ADMIN"]}>
                <SystemSetup />
              </ProtectedRoute>
            }
          />
          <Route
            path="/unauthorized"
            element={
              <div style={{ textAlign: "center", padding: "3rem" }}>
                <h2>Access Denied</h2>
                <p>You don't have permission to view this page.</p>
              </div>
            }
          />

          {/* Admin routes */}
          <Route
            path="/admin/dashboard"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <AdminDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/users"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <AdminUsers />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/companies"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <AdminCompanies />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/branches"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <AdminBranches />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/services"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <AdminServices />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/activity"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <ActivityLog adminView={true} />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/reports"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <ActivityLog adminView={true} />
              </ProtectedRoute>
            }
          />

          {/* Branch Manager routes */}
          <Route
            path="/manager/dashboard"
            element={
              <ProtectedRoute allowedRoles={["BRANCH_MANAGER"]}>
                <ManagerDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/manager/requests"
            element={
              <ProtectedRoute allowedRoles={["BRANCH_MANAGER"]}>
                <ManagerDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/manager/tokens"
            element={
              <ProtectedRoute allowedRoles={["BRANCH_MANAGER"]}>
                <VerifyTokens />
              </ProtectedRoute>
            }
          />
          <Route
            path="/manager/technicians"
            element={
              <ProtectedRoute allowedRoles={["BRANCH_MANAGER"]}>
                <ManagerTechnicians />
              </ProtectedRoute>
            }
          />
          <Route
            path="/manager/completions"
            element={
              <ProtectedRoute allowedRoles={["BRANCH_MANAGER"]}>
                <ManagerCompletions />
              </ProtectedRoute>
            }
          />
          <Route
            path="/manager/reports"
            element={
              <ProtectedRoute allowedRoles={["BRANCH_MANAGER"]}>
                <ActivityLog adminView={false} />
              </ProtectedRoute>
            }
          />
          <Route
            path="/manager/activity"
            element={
              <ProtectedRoute allowedRoles={["BRANCH_MANAGER"]}>
                <ActivityLog adminView={false} />
              </ProtectedRoute>
            }
          />

          {/* Technician routes */}
          <Route
            path="/technician/dashboard"
            element={
              <ProtectedRoute allowedRoles={["TECHNICIAN"]}>
                <TechnicianDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/technician/requests"
            element={
              <ProtectedRoute allowedRoles={["TECHNICIAN"]}>
                <TechnicianRequests />
              </ProtectedRoute>
            }
          />
          <Route
            path="/technician/tokens"
            element={
              <ProtectedRoute allowedRoles={["TECHNICIAN"]}>
                <TechnicianTokens />
              </ProtectedRoute>
            }
          />
          <Route
            path="/technician/completions"
            element={
              <ProtectedRoute allowedRoles={["TECHNICIAN"]}>
                <TechnicianCompletions />
              </ProtectedRoute>
            }
          />
          <Route
            path="/technician/activity"
            element={
              <ProtectedRoute allowedRoles={["TECHNICIAN"]}>
                <ActivityLog adminView={false} />
              </ProtectedRoute>
            }
          />

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
