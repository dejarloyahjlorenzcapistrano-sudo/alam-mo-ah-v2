import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function LibraryPage() {
  const { user, isLoggedIn, getBookmarks, removeBookmark } = useAuth();
  const [bookmarks, setBookmarks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('all');
  const [copied, setCopied] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoggedIn) { navigate('/'); return; }
    getBookmarks()
      .then(setBookmarks)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, [isLoggedIn]);

  const handleRemove = async (entryId) => {
    await removeBookmark(entryId);
    setBookmarks(b => b.filter(bm => bm.entryId !== entryId));
  };

  const copyToClipboard = (text, id) => {
    navigator.clipboard.writeText(text).then(() => {
      setCopied(id);
      setTimeout(() => setCopied(null), 2000);
    });
  };

  const languages = ['all', ...new Set(bookmarks.map(b => b.languageName))];
  const filtered = filter === 'all' ? bookmarks : bookmarks.filter(b => b.languageName === filter);

  if (!isLoggedIn) return null;

  return (
    <div className="page library-page">
      <div className="library-header">
        <div>
          <h1 className="library-title">
            <span className="t-accent">// </span>
            {user?.username}'s library
          </h1>
          <p className="library-sub">
            {bookmarks.length} saved syntax{bookmarks.length !== 1 ? 'es' : ''}
          </p>
        </div>
        <Link to="/" className="back-link">← browse more</Link>
      </div>

      {bookmarks.length > 0 && (
        <div className="library-filters">
          {languages.map(lang => (
            <button
              key={lang}
              className={`filter-btn ${filter === lang ? 'active' : ''}`}
              onClick={() => setFilter(lang)}
            >
              {lang}
            </button>
          ))}
        </div>
      )}

      {loading && <p className="loading-text">fetching your library<span className="blink">...</span></p>}

      {!loading && bookmarks.length === 0 && (
        <div className="empty-state">
          <p className="empty-icon">📚</p>
          <p className="empty-text">Your library is empty.</p>
          <p className="empty-sub">Start browsing and click <strong>☆ save</strong> on any syntax to add it here.</p>
          <Link to="/" className="empty-cta">browse languages →</Link>
        </div>
      )}

      <div className="entries-list">
        {filtered.map(bm => (
          <div key={bm.id} className="entry-card library-card">
            <div className="entry-header">
              <div>
                <span
                  className="result-lang-badge"
                  style={{ '--lang-color': bm.languageColor }}
                >
                  {bm.languageName}
                </span>
                <span className="result-cat">{bm.categoryTitle}</span>
                <h3 className="entry-title">{bm.entryTitle}</h3>
              </div>
              <div className="library-card-actions">
                <Link
                  to={`/language/${bm.languageId}`}
                  className="view-full-link"
                >
                  view full →
                </Link>
                <button
                  className="remove-bookmark-btn"
                  onClick={() => handleRemove(bm.entryId)}
                  title="Remove from library"
                >
                  ★ saved
                </button>
              </div>
            </div>
            <p className="entry-desc">{bm.entryDescription}</p>
            <p className="bookmark-date">
              saved {new Date(bm.bookmarkedAt).toLocaleDateString('en-PH', {
                year: 'numeric', month: 'short', day: 'numeric'
              })}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}
