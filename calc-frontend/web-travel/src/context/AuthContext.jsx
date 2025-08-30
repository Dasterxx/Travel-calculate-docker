import React, { useState, useEffect, createContext, useContext } from 'react';
import { fetchWithLogging } from '../utils/fetchWithLogging';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      fetchUserProfile(token);
    } else {
      setLoading(false);
    }
  }, []);

  const fetchUserProfile = async (token) => {
    try {
      const response = await fetchWithLogging('/api/auth/me', {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        const data = await response.json();
        setUser(data.data);
      } else {
        localStorage.removeItem('token');
        setUser(null);
      }
    } catch (error) {
      console.error('Failed to fetch user profile:', error);
      localStorage.removeItem('token');
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  const login = async (email, password) => {
    try {
      const response = await fetchWithLogging('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        return { success: false, message: errorData.message || 'Invalid email or password' };
      }

      const data = await response.json();
      localStorage.setItem('token', data.data.token);
      await fetchUserProfile(data.data.token);
      return { success: true };
    } catch (error) {
      return { success: false, message: error.message || 'Login failed. Please try again.' };
    }
  };

  const register = async (formData) => {
    try {
      const response = await fetchWithLogging('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        if (errorData.errors && Array.isArray(errorData.errors)) {
          const messages = errorData.errors.map(e => e.description || e.errorCode).join(', ');
          return { success: false, message: messages };
        }
        return { success: false, message: errorData.message || 'Registration failed' };
      }

      return { success: true };
    } catch (error) {
      return { success: false, message: error.message || 'Registration error. Please check your data.' };
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, logout, register }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
