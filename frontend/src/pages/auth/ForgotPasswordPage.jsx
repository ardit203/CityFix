import { useState } from 'react';
import { Link } from 'react-router-dom';
import InputField from '../../components/forms/InputField';
import Button from '../../components/common/Button';
import FormError from '../../components/forms/FormError';

export default function ForgotPasswordPage() {
  const [email, setEmail] = useState('');
  const [submitted, setSubmitted] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (!email.trim()) { setError('Please enter your email address.'); return; }
    setLoading(true);
    // Endpoint to be implemented by backend team
    setTimeout(() => { setSubmitted(true); setLoading(false); }, 800);
  };

  if (submitted) {
    return (
      <div className="text-center">
        <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
          </svg>
        </div>
        <h3 className="font-semibold text-slate-900 mb-1">Check your email</h3>
        <p className="text-sm text-slate-500 mb-6">If that email is registered, you'll receive a reset link shortly.</p>
        <Link to="/login" className="text-primary-600 hover:text-primary-700 text-sm font-medium">← Back to Login</Link>
      </div>
    );
  }

  return (
    <>
      <div className="mb-6">
        <h2 className="text-xl font-bold text-slate-900">Reset your password</h2>
        <p className="text-sm text-slate-500 mt-1">Enter your email and we'll send you a reset link.</p>
      </div>

      <form onSubmit={handleSubmit} className="flex flex-col gap-4">
        <InputField id="email" label="Email address" type="email"
          value={email} onChange={(e) => setEmail(e.target.value)}
          placeholder="you@example.com" required disabled={loading} />
        <FormError message={error} />
        <Button type="submit" disabled={loading}>Send Reset Link</Button>
      </form>

      <p className="mt-6 text-center text-sm text-slate-500">
        <Link to="/login" className="text-primary-600 hover:text-primary-700 font-medium">← Back to Login</Link>
      </p>
    </>
  );
}
