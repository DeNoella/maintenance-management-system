import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

// Attach JWT token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Handle 401 globally
api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.clear();
      window.location.href = '/login';
    }
    return Promise.reject(err);
  }
);

// ─── Auth ───
export const authAPI = {
  login: (username, password) => api.post('/auth/login', { username, password }),
};

// ─── Users ───
export const userAPI = {
  getAll: () => api.get('/users'),
  getById: (id) => api.get(`/users/${id}`),
  create: (data) => api.post('/users', data),
  createTechnician: (data) => api.post('/users/technician', data),
  deactivate: (id) => api.put(`/users/${id}/deactivate`),
  getByBranch: (branchId) => api.get(`/users/branch/${branchId}`),
};

// ─── Companies ───
export const companyAPI = {
  getAll: () => api.get('/companies'),
  getById: (id) => api.get(`/companies/${id}`),
  create: (data) => api.post('/companies', data),
  update: (id, data) => api.put(`/companies/${id}`, data),
  deactivate: (id) => api.put(`/companies/${id}/deactivate`),
};

// ─── Branches ───
export const branchAPI = {
  getAll: () => api.get('/branches'),
  getById: (id) => api.get(`/branches/${id}`),
  getByCompany: (companyId) => api.get(`/branches/company/${companyId}`),
  create: (data) => api.post('/branches', data),
  update: (id, data) => api.put(`/branches/${id}`, data),
  deactivate: (id) => api.put(`/branches/${id}/deactivate`),
};

// ─── Services ───
export const serviceAPI = {
  getAll: () => api.get('/services'),
  create: (data) => api.post('/services', data),
};

// ─── Maintenance Requests ───
export const requestAPI = {
  getAll: () => api.get('/requests'),
  getByTechnician: (id) => api.get(`/requests/technician/${id}`),
  getByBranch: (id) => api.get(`/requests/branch/${id}`),
  submit: (data) => api.post('/requests', data),
  updateStatus: (id, status) => api.put(`/requests/${id}/status`, { status }),
};

// ─── Access Tokens ───
export const tokenAPI = {
  getByTechnician: (id) => api.get(`/tokens/technician/${id}`),
  getByBranch: (id) => api.get(`/tokens/branch/${id}`),
  verify: (tokenCode) => api.get(`/tokens/verify/${tokenCode}`),
  decide: (id, decision, notes) => api.post(`/tokens/${id}/verify`, { decision, notes }),
};

// ─── Completion Reports ───
export const reportAPI = {
  submit: (data) => api.post('/reports', data),
  getByTechnician: (id) => api.get(`/reports/technician/${id}`),
  getByStatus: (status) => api.get(`/reports/status/${status}`),
  approve: (id) => api.put(`/reports/${id}/approve`),
  reject: (id) => api.put(`/reports/${id}/reject`),
};

// ─── Activity Logs ───
export const activityAPI = {
  getByUser: (id) => api.get(`/activity/user/${id}`),
  getAll: () => api.get('/activity'),
};

export default api;
