import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { register } from '../../services/authService';
import InputField from '../../components/forms/InputField';
import SelectField from '../../components/forms/SelectField';
import Button from '../../components/common/Button';
import FormError from '../../components/forms/FormError';

/**
 * Role options — only CITIZEN is selectable during self-registration.
 * Staff accounts (EMPLOYEE, MANAGER) are created by admins.
 */
// const ROLE_OPTIONS = [
//   { value: 'ROLE_CITIZEN', label: 'Citizen' },
// ];

const INITIAL_FORM = {
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  role: 'ROLE_CITIZEN',
  name: '',
  surname: '',
  city: '',
  street: '',
  postalCode: '',
};

export default function RegisterPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState(INITIAL_FORM);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.id]: e.target.value }));
    setError('');
  };

  const validate = () => {
    if (!form.username.trim()) return 'Username is required.';
    if (!form.email.trim()) return 'Email is required.';
    if (!form.password) return 'Password is required.';
    if (form.password !== form.confirmPassword) return 'Passwords do not match.';
    if (!form.name.trim()) return 'First name is required.';
    if (!form.surname.trim()) return 'Last name is required.';
    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const validationError = validate();
    if (validationError) {
      setError(validationError);
      return;
    }

    setLoading(true);
    try {
      // Exclude confirmPassword — not sent to backend
      const { confirmPassword, ...payload } = form;
      await register(payload);
      // On success redirect to login
      navigate('/login', { state: { registered: true } });
    } catch (err) {
      const msg = err?.response?.data?.message;
      setError(msg || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <div className="mb-6">
        <h2 className="text-xl font-bold text-slate-900">Create your account</h2>
        <p className="text-sm text-slate-500 mt-1">Join CityFix to report and track city issues</p>
      </div>

      <form onSubmit={handleSubmit} className="flex flex-col gap-4" noValidate>

        {/* Account info */}
        <div className="flex flex-col gap-4">
          <p className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Account Details</p>

          <InputField
            id="username"
            label="Username"
            value={form.username}
            onChange={handleChange}
            placeholder="Choose a username"
            required
            autoComplete="username"
            disabled={loading}
          />

          <InputField
            id="email"
            label="Email"
            type="email"
            value={form.email}
            onChange={handleChange}
            placeholder="you@example.com"
            required
            autoComplete="email"
            disabled={loading}
          />

          <InputField
            id="password"
            label="Password"
            type="password"
            value={form.password}
            onChange={handleChange}
            placeholder="Create a password"
            required
            autoComplete="new-password"
            disabled={loading}
          />

          <InputField
            id="confirmPassword"
            label="Confirm Password"
            type="password"
            value={form.confirmPassword}
            onChange={handleChange}
            placeholder="Repeat your password"
            required
            autoComplete="new-password"
            disabled={loading}
          />
        </div>

        {/* Personal info */}
        <div className="flex flex-col gap-4 pt-2">
          <p className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Personal Details</p>

          <div className="grid grid-cols-2 gap-3">
            <InputField
              id="name"
              label="First Name"
              value={form.name}
              onChange={handleChange}
              placeholder="John"
              required
              disabled={loading}
            />
            <InputField
              id="surname"
              label="Last Name"
              value={form.surname}
              onChange={handleChange}
              placeholder="Doe"
              required
              disabled={loading}
            />
          </div>
        </div>

        {/* Address info */}
        <div className="flex flex-col gap-4 pt-2">
          <p className="text-xs font-semibold text-slate-400 uppercase tracking-wider">Address <span className="normal-case font-normal text-slate-400">(optional)</span></p>

          <InputField
            id="street"
            label="Street"
            value={form.street}
            onChange={handleChange}
            placeholder="123 Main St"
            disabled={loading}
          />

          <div className="grid grid-cols-2 gap-3">
            <InputField
              id="city"
              label="City"
              value={form.city}
              onChange={handleChange}
              placeholder="Skopje"
              disabled={loading}
            />
            <InputField
              id="postalCode"
              label="Postal Code"
              value={form.postalCode}
              onChange={handleChange}
              placeholder="1000"
              disabled={loading}
            />
          </div>
        </div>

        {/* Role (hidden for now, citizens only) */}
        {/* <SelectField
          id="role"
          label="Account Type"
          value={form.role}
          onChange={handleChange}
          options={ROLE_OPTIONS}
          disabled={loading}
        /> */}

        <FormError message={error} />

        <Button type="submit" disabled={loading} className="mt-2">
          Create Account
        </Button>
      </form>

      <p className="mt-6 text-center text-sm text-slate-500">
        Already have an account?{' '}
        <Link
          to="/login"
          className="text-primary-600 hover:text-primary-700 font-semibold transition-colors"
        >
          Sign in
        </Link>
      </p>
    </>
  );
}
