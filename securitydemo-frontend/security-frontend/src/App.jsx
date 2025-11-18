import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

const App = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const [message, setMessage] = useState('');
  const [showRegister, setShowRegister] = useState(false);

  // Configure axios
  const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080',
  });

  useEffect(() => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
      setIsLoggedIn(true);
    }
  }, []);

  const handleLogin = async (username, password) => {
    try {
      setMessage('Logging in...');
      
      // Create Basic Auth header
      const authHeader = 'Basic ' + btoa(username + ':' + password);
      
      const response = await axiosInstance.get('/api/test/secured', {
        headers: {
          'Authorization': authHeader,
          'Content-Type': 'application/json'
        }
      });

      if (response.status === 200) {
        const userInfo = { username, password };
        setUser(userInfo);
        setIsLoggedIn(true);
        localStorage.setItem('user', JSON.stringify(userInfo));
        setMessage('Login successful!');
      }
    } catch (error) {
      console.error('Login error:', error);
      if (error.response) {
        setMessage(`Login failed: ${error.response.status} - ${error.response.statusText}`);
      } else if (error.request) {
        setMessage('Network error: Cannot connect to server. Check if Spring Boot is running on port 8080.');
      } else {
        setMessage('Login failed: ' + error.message);
      }
    }
  };

  const handleRegister = async (username, password, email) => {
    try {
      setMessage('Registering...');
      
      const response = await axiosInstance.post('/api/auth/register', {
        username,
        password,
        email
      }, {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      if (response.status === 200) {
        setMessage('Registration successful! Please login.');
        setShowRegister(false);
      }
    } catch (error) {
      console.error('Registration error:', error);
      if (error.response) {
        setMessage(`Registration failed: ${error.response.data?.error || error.response.statusText}`);
      } else {
        setMessage('Registration failed: ' + error.message);
      }
    }
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setUser(null);
    setMessage('');
    localStorage.removeItem('user');
  };

  const testPublicEndpoint = async () => {
    try {
      const response = await axiosInstance.get('/api/test/public');
      setMessage('Public endpoint: ' + response.data.message);
    } catch (error) {
      setMessage('Error: ' + error.message);
    }
  };

  const testSecuredEndpoint = async () => {
    try {
      const authHeader = 'Basic ' + btoa(user.username + ':' + user.password);
      const response = await axiosInstance.get('/api/test/secured', {
        headers: {
          'Authorization': authHeader
        }
      });
      setMessage('Secured endpoint: ' + response.data.message);
    } catch (error) {
      setMessage('Error accessing secured endpoint: ' + error.message);
    }
  };

  const testAdminEndpoint = async () => {
    try {
      const authHeader = 'Basic ' + btoa(user.username + ':' + user.password);
      const response = await axiosInstance.get('/api/test/admin', {
        headers: {
          'Authorization': authHeader
        }
      });
      setMessage('Admin endpoint: ' + response.data.message);
    } catch (error) {
      setMessage('Error accessing admin endpoint: ' + (error.response?.data?.message || 'Access denied'));
    }
  };

  if (isLoggedIn) {
    return (
      <div className="app">
        <div className="container">
          <h1>Spring Security + React Demo</h1>
          <div className="welcome">
            <h2>Welcome, {user.username}!</h2>
            <button onClick={handleLogout} className="btn btn-logout">Logout</button>
          </div>
          
          <div className="button-group">
            <button onClick={testPublicEndpoint} className="btn btn-public">Test Public Endpoint</button>
            <button onClick={testSecuredEndpoint} className="btn btn-secured">Test Secured Endpoint</button>
            <button onClick={testAdminEndpoint} className="btn btn-admin">Test Admin Endpoint</button>
          </div>

          {message && <div className="message">{message}</div>}
        </div>
      </div>
    );
  }

  return (
    <div className="app">
      <div className="container">
        <h1>Spring Security + React Demo</h1>
        
        {!showRegister ? (
          <LoginForm 
            onLogin={handleLogin} 
            onSwitchToRegister={() => setShowRegister(true)}
            message={message}
          />
        ) : (
          <RegisterForm 
            onRegister={handleRegister} 
            onSwitchToLogin={() => setShowRegister(false)}
            message={message}
          />
        )}
      </div>
    </div>
  );
};

const LoginForm = ({ onLogin, onSwitchToRegister, message }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onLogin(username, password);
  };

  return (
    <div className="auth-form">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit" className="btn btn-login">Login</button>
      </form>
      <p>
        Don't have an account?{' '}
        <button onClick={onSwitchToRegister} className="link-btn">Register here</button>
      </p>
      <div className="test-accounts">
        <p><strong>Test Accounts:</strong></p>
        <p>Regular user: testuser / password123</p>
        <p>Admin user: admin / admin123</p>
      </div>
      {message && <div className="message">{message}</div>}
    </div>
  );
};

const RegisterForm = ({ onRegister, onSwitchToLogin, message }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onRegister(username, password, email);
  };

  return (
    <div className="auth-form">
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit" className="btn btn-register">Register</button>
      </form>
      <p>
        Already have an account?{' '}
        <button onClick={onSwitchToLogin} className="link-btn">Login here</button>
      </p>
      {message && <div className="message">{message}</div>}
    </div>
  );
};

export default App;