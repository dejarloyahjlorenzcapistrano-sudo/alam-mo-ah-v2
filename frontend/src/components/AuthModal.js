import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';

export default function AuthModal({ onClose }) {
  const { login, register, loading } = useAuth();
  const [mode, setMode] = useState('login'); // 'login' | 'register'
  const [form, setForm] = useState({ username: '', email: '', password: '', course: '' });
  const [error, setError] = useState('');

  const set = (k, v) => setForm(f => ({ ...f, [k]: v }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    let result;
    if (mode === 'login') {
      result = await login(form.username, form.password);
    } else {
      result = await register(form.username, form.email, form.password, form.course);
    }
    if (result.success) onClose();
    else setError(result.error);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-box" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <div className="modal-tabs">
            <button
              className={`modal-tab ${mode === 'login' ? 'active' : ''}`}
              onClick={() => { setMode('login'); setError(''); }}
            >login</button>
            <button
              className={`modal-tab ${mode === 'register' ? 'active' : ''}`}
              onClick={() => { setMode('register'); setError(''); }}
            >register</button>
          </div>
          <button className="modal-close" onClick={onClose}>✕</button>
        </div>

        <div className="modal-terminal-bar">
          <span className="t-prompt">$</span>
          <span className="t-cmd">
            {mode === 'login' ? 'auth --login' : 'auth --register'}
          </span>
        </div>

        <form className="modal-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label className="form-label">username</label>
            <input
              className="form-input"
              type="text"
              placeholder="your_username"
              value={form.username}
              onChange={e => set('username', e.target.value)}
              required
              autoComplete="username"
            />
          </div>

          {mode === 'register' && (
            <div className="form-group">
              <label className="form-label">email</label>
              <input
                className="form-input"
                type="email"
                placeholder="you@school.edu"
                value={form.email}
                onChange={e => set('email', e.target.value)}
                required
              />
            </div>
          )}

          <div className="form-group">
            <label className="form-label">password</label>
            <input
              className="form-input"
              type="password"
              placeholder="••••••••"
              value={form.password}
              onChange={e => set('password', e.target.value)}
              required
              autoComplete={mode === 'login' ? 'current-password' : 'new-password'}
            />
          </div>

          {mode === 'register' && (
            <div className="form-group">
              <label className="form-label">course <span className="form-optional">(optional)</span></label>
              <select
                className="form-input form-select"
                value={form.course}
                onChange={e => set('course', e.target.value)}
              >
                <option value="">Select your course</option>
                <option value="BSIT">BSIT</option>
                <option value="BSCS">BSCS</option>
                <option value="BSIS">BSIS</option>
                <option value="BSECE">BSECE</option>
                <option value="Other">Other</option>
              </select>
            </div>
          )}

          {error && <p className="form-error">⚠ {error}</p>}

          <button className="form-submit" type="submit" disabled={loading}>
            {loading ? 'loading...' : mode === 'login' ? 'login →' : 'create account →'}
          </button>
        </form>

        <p className="modal-switch">
          {mode === 'login'
            ? <>No account? <button className="modal-switch-btn" onClick={() => setMode('register')}>register here</button></>
            : <>Have an account? <button className="modal-switch-btn" onClick={() => setMode('login')}>login here</button></>
          }
        </p>
      </div>
    </div>
  );
}
