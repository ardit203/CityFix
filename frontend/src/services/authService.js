import api from './api';

/**
 * POST /api/user/login
 * Body: { username, password }
 * Response: { token }
 */
export const login = async ({ username, password }) => {
  const response = await api.post('/api/user/login', { username, password });
  return response.data; // { token }
};

/**
 * POST /api/user/register
 * Body: { username, password, email, role, name, surname, city, street, postalCode }
 * Response: { username, email, role, name, surname, profilePictureUrl }
 */
export const register = async (payload) => {
  const response = await api.post('/api/user/register', payload);
  return response.data;
};
