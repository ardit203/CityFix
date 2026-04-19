import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { login } from '../../services/authService';
import InputField from '../../components/forms/InputField';
import Button from '../../components/common/Button';
import FormError from '../../components/forms/FormError';

// Role → dashboard path mapping
const ROLE_REDIRECT = {
  CITIZEN: '/citizen/dashboard',
  EMPLOYEE: '/employee/dashboard',
  MANAGER: '/manager/dashboard',
  ADMIN: '/admin/dashboard',
};

export default function LoginPage() {
  const navigate = useNavigate();
  const { saveToken, role: currentRole } = useAuth();

  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.id]: e.target.value }));
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!form.username.trim() || !form.password.trim()) {
      setError('Please enter your username and password.');
      return;
    }

    setLoading(true);
    try {
      const data = await login({ username: form.username.trim(), password: form.password });
      saveToken(data.token);

      // Decode role from just-saved token
      const payload = JSON.parse(atob(data.token.split('.')[1]));
      const role = payload?.role?.replace('ROLE_', '').replace('ADMINISTRATOR', 'ADMIN');
      navigate(ROLE_REDIRECT[role] ?? '/');
    } catch (err) {
      const msg = err?.response?.data?.message;
      setError(msg || 'Invalid username or password.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <div className="mb-6">
        <h2 className="text-xl font-bold text-slate-900">Welcome back</h2>
        <p className="text-sm text-slate-500 mt-1">Sign in to your CityFix account</p>
      </div>

      <form onSubmit={handleSubmit} className="flex flex-col gap-4" noValidate>
        <InputField
          id="username"
          label="Username"
          type="text"
          value={form.username}
          onChange={handleChange}
          placeholder="Enter your username"
          required
          autoComplete="username"
          disabled={loading}
        />

        <InputField
          id="password"
          label="Password"
          type="password"
          value={form.password}
          onChange={handleChange}
          placeholder="Enter your password"
          required
          autoComplete="current-password"
          disabled={loading}
        />

        <FormError message={error} />

        <div className="flex items-center justify-end">
          <Link
            to="/forgot-password"
            className="text-sm text-primary-600 hover:text-primary-700 font-medium transition-colors"
          >
            Forgot password?
          </Link>
        </div>

        <Button type="submit" disabled={loading}>
          Sign In
        </Button>
      </form>

      <p className="mt-6 text-center text-sm text-slate-500">
        Don&apos;t have an account?{' '}
        <Link
          to="/register"
          className="text-primary-600 hover:text-primary-700 font-semibold transition-colors"
        >
          Register
        </Link>
      </p>
    </>
  );
}
