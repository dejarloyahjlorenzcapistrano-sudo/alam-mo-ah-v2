import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(null);
const API = "https://alam-mo-ah-v2-production.up.railway.app/api";

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('ama_token'));
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const stored = localStorage.getItem('ama_user');
    if (stored && token) setUser(JSON.parse(stored));
  }, [token]);

  const authHeaders = () => ({
    'Content-Type': 'application/json',
    ...(token ? { Authorization: `Bearer ${token}` } : {})
  });

  const login = async (username, password) => {
    setLoading(true);
    try {
      const res = await fetch(`${API}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.error || 'Login failed');
      setToken(data.token);
      setUser(data);
      localStorage.setItem('ama_token', data.token);
      localStorage.setItem('ama_user', JSON.stringify(data));
      return { success: true };
    } catch (err) {
      return { success: false, error: err.message };
    } finally {
      setLoading(false);
    }
  };

  const register = async (username, email, password, course) => {
    setLoading(true);

    try {
      const res = await fetch(`${API}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, email, password, course })
      });

      const text = await res.text();
      console.log(text);

      let data = {};

      try {
        data = JSON.parse(text);
      } catch {}

      if (!res.ok) {
        throw new Error(data.error || text || 'Registration failed');
      }

      setToken(data.token);
      setUser(data);

      localStorage.setItem('ama_token', data.token);
      localStorage.setItem('ama_user', JSON.stringify(data));

      return { success: true };

    } catch (err) {
      return { success: false, error: err.message };
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('ama_token');
    localStorage.removeItem('ama_user');
  };

  // Bookmark API calls
  const getBookmarks = async () => {
    const res = await fetch(`${API}/bookmarks`, { headers: authHeaders() });
    if (!res.ok) throw new Error('Failed to fetch bookmarks');
    return res.json();
  };

  const addBookmark = async (entry) => {
    const res = await fetch(`${API}/bookmarks`, {
      method: 'POST',
      headers: authHeaders(),
      body: JSON.stringify(entry)
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || 'Failed to bookmark');
    if (user) {
      const updated = { ...user, bookmarkCount: (user.bookmarkCount || 0) + 1 };
      setUser(updated);
      localStorage.setItem('ama_user', JSON.stringify(updated));
    }
    return data;
  };

  const removeBookmark = async (entryId) => {
    const res = await fetch(`${API}/bookmarks/${entryId}`, {
      method: 'DELETE',
      headers: authHeaders()
    });
    if (!res.ok) throw new Error('Failed to remove bookmark');
    if (user) {
      const updated = { ...user, bookmarkCount: Math.max(0, (user.bookmarkCount || 0) - 1) };
      setUser(updated);
      localStorage.setItem('ama_user', JSON.stringify(updated));
    }
  };

  const checkBookmark = async (entryId) => {
    if (!token) return false;
    try {
      const res = await fetch(`${API}/bookmarks/check/${entryId}`, { headers: authHeaders() });
      const data = await res.json();
      return data.bookmarked;
    } catch { return false; }
  };

  return (
    <AuthContext.Provider value={{
      user, token, loading,
      login, register, logout,
      getBookmarks, addBookmark, removeBookmark, checkBookmark,
      isLoggedIn: !!token
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
export { API };
