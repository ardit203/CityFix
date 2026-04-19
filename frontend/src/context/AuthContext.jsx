import { createContext, useContext, useState, useCallback } from 'react';

const AuthContext = createContext(null);

/**
 * Decodes a JWT payload (base64) without a library.
 * Returns null if the token is invalid.
 */
function decodeToken(token) {
  try {
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload));
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('token'));
  const [user, setUser] = useState(() => {
    const t = localStorage.getItem('token');
    return t ? decodeToken(t) : null;
  });

  const saveToken = useCallback((newToken) => {
    localStorage.setItem('token', newToken);
    setToken(newToken);
    setUser(decodeToken(newToken));
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  }, []);

  /**
   * role is stored in JWT as e.g. "ROLE_CITIZEN"
   * We normalise to our frontend keys: CITIZEN, EMPLOYEE, MANAGER, ADMIN
   */
  const role = user?.role?.replace('ROLE_', '').replace('ADMINISTRATOR', 'ADMIN') ?? null;

  return (
    <AuthContext.Provider value={{ token, user, role, saveToken, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
