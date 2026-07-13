import { useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';

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
        
        const token = response.data.token;
        localStorage.setItem('authToken', token);
        localStorage.setItem('userId', response.data.userId);
        
        setStatusMessage('Login successful! Loading your profile...');
        setTimeout(() => {
          navigate('/profile');
        }, 1000);
    } catch (err) {
        setError(
          err.response?.data?.message ||
            err.response?.data ||
            err.message ||
            'Unable to log in. Please try again.'
        );
        setIsLoggingIn(false);
    }
  };

  return (
    <div style={{ maxWidth: '400px', margin: '50px auto', padding: '20px', border: '1px solid #ccc' }}>
      <h2>Outdoor Matchmaker Login</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {statusMessage && <p>{statusMessage}</p>}
      {isLoggingIn && (
        <p>Please wait. The server may take a moment to respond.</p>
      )}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Email:</label>
          <input type="email" name="email" value={credentials.email} onChange={handleChange} required />
        </div>
        <div>
          <label>Password:</label>
          <input type="password" name="password" value={credentials.password} onChange={handleChange} required />
        </div>
        <button type="submit" disabled={isLoggingIn}>
          {isLoggingIn ? 'Logging in...' : 'Log In'}
        </button>
      </form>
      <p>
        Don't have an account? <Link to="/register">Register</Link>
      </p>
    </div>
  );
}
