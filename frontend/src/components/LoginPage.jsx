import { useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Auth.css';

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

export default function LoginPage() {
  const navigate = useNavigate();
  const [credentials, setCredentials] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [statusMessage, setStatusMessage] = useState('');
  const [isLoggingIn, setIsLoggingIn] = useState(false);

  const handleChange = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoggingIn(true);
    setError('');
    setStatusMessage('');

    try {
      const response = await axios.post(`${API_BASE_URL}/auth/login`, credentials);
      const { token, userId } = response.data;

      localStorage.setItem('authToken', token);
      localStorage.setItem('userId', userId);

      setStatusMessage('Login successful! Redirecting...');
      setTimeout(() => {
        navigate('/profile');
      }, 1000);
    } catch (err) {
      setError(
        err.response?.data?.message || 'Unable to log in. Please try again.'
      );
      setIsLoggingIn(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <header className="auth-card__header">
          <h2 className="auth-card__title">Welcome back</h2>
          <p className="auth-card__subtitle">
            Sign in to Outty and find your next adventure
          </p>
        </header>

        {error && (
          <p className="auth-message auth-message--error" role="alert">
            {error}
          </p>
        )}
        {statusMessage && (
          <p className="auth-message auth-message--success">{statusMessage}</p>
        )}
        {isLoggingIn && !statusMessage && (
          <p className="auth-message auth-message--hint">
            Please wait. The server may take a moment to respond.
          </p>
        )}

        <form className="auth-form" onSubmit={handleSubmit}>
          <div className="auth-field">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              type="email"
              name="email"
              value={credentials.email}
              onChange={handleChange}
              required
              disabled={isLoggingIn}
              autoComplete="email"
            />
          </div>

          <div className="auth-field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              name="password"
              value={credentials.password}
              onChange={handleChange}
              required
              disabled={isLoggingIn}
              autoComplete="current-password"
            />
          </div>

          <button className="auth-button" type="submit" disabled={isLoggingIn}>
            {isLoggingIn ? 'Logging in...' : 'Log In'}
          </button>
        </form>

        <p className="auth-footer">
          Don&apos;t have an account? <Link to="/register">Register</Link>
        </p>
      </div>
    </div>
  );
}
