import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';

export default function BookmarkButton({ entry, language, category, onAuthRequired }) {
  const { isLoggedIn, addBookmark, removeBookmark, checkBookmark } = useAuth();
  const [bookmarked, setBookmarked] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isLoggedIn && entry?.id) {
      checkBookmark(entry.id).then(setBookmarked);
    } else {
      setBookmarked(false);
    }
  }, [isLoggedIn, entry?.id]);

  const toggle = async () => {
    if (!isLoggedIn) { onAuthRequired?.(); return; }
    setLoading(true);
    try {
      if (bookmarked) {
        await removeBookmark(entry.id);
        setBookmarked(false);
      } else {
        await addBookmark({
          entryId: entry.id,
          languageId: language.id,
          languageName: language.name,
          languageColor: language.color,
          entryTitle: entry.title,
          entryDescription: entry.description,
          categoryTitle: category
        });
        setBookmarked(true);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <button
      className={`bookmark-btn ${bookmarked ? 'bookmarked' : ''}`}
      onClick={toggle}
      disabled={loading}
      title={!isLoggedIn ? 'Login to bookmark' : bookmarked ? 'Remove bookmark' : 'Bookmark this'}
    >
      {loading ? '...' : bookmarked ? '★ saved' : '☆ save'}
    </button>
  );
}
