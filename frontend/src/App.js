import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate, useParams } from 'react-router-dom';
import { AuthProvider, useAuth, API } from './context/AuthContext';
import AuthModal from './components/AuthModal';
import BookmarkButton from './components/BookmarkButton';
import LibraryPage from './pages/LibraryPage';
import './App.css';

// ─── NAVBAR ──────────────────────────────────────────────────────────────────
function Navbar({ searchQuery, setSearchQuery, onAuthOpen }) {
  const { user, isLoggedIn, logout } = useAuth();
  const navigate = useNavigate();
  const [userMenuOpen, setUserMenuOpen] = useState(false);

  const handleSearch = (e) => {
    e.preventDefault();
    if (searchQuery.trim()) navigate(`/search?q=${encodeURIComponent(searchQuery)}`);
  };

  return (
    <nav className="navbar">
      <Link to="/" className="nav-logo">
        <span className="logo-bracket">[</span>
        alam<span className="logo-accent">mo</span>ah
        <span className="logo-bracket">]</span>
      </Link>

      <form className="search-form" onSubmit={handleSearch}>
        <span className="search-prefix">$</span>
        <input
          type="text"
          placeholder="search syntax..."
          value={searchQuery}
          onChange={e => setSearchQuery(e.target.value)}
          className="search-input"
        />
      </form>

      <div className="nav-links">
        <Link to="/" className="nav-link">home</Link>
        {isLoggedIn && <Link to="/library" className="nav-link">library {user?.bookmarkCount > 0 && <span className="nav-badge">{user.bookmarkCount}</span>}</Link>}
        <Link to="/about" className="nav-link">about</Link>

        {isLoggedIn ? (
          <div className="user-menu-wrap">
            <button className="user-menu-btn" onClick={() => setUserMenuOpen(o => !o)}>
              <span className="user-avatar">{user?.username?.[0]?.toUpperCase()}</span>
              <span className="user-name">{user?.username}</span>
              <span className="user-caret">▾</span>
            </button>
            {userMenuOpen && (
              <div className="user-dropdown">
                <div className="user-dropdown-info">
                  <p className="dropdown-username">{user?.username}</p>
                  {user?.course && <p className="dropdown-course">{user.course}</p>}
                </div>
                <Link to="/library" className="dropdown-item" onClick={() => setUserMenuOpen(false)}>
                  ★ my library ({user?.bookmarkCount || 0})
                </Link>
                <button className="dropdown-item dropdown-logout" onClick={() => { logout(); setUserMenuOpen(false); }}>
                  logout
                </button>
              </div>
            )}
          </div>
        ) : (
          <button className="nav-login-btn" onClick={onAuthOpen}>login</button>
        )}
      </div>
    </nav>
  );
}

// ─── HOME ─────────────────────────────────────────────────────────────────────
function HomePage({ onAuthOpen }) {
  const [languages, setLanguages] = useState([]);
  const [loading, setLoading] = useState(true);
  const taglines = ['syntax wiki para sa mga IT students.', 'no BS. just code.', "alam mo na ba 'to?", 'copy. paste. pass.'];
  const [tagline] = useState(taglines[Math.floor(Math.random() * taglines.length)]);

  useEffect(() => {
    fetch(`${API}/languages`).then(r => r.json()).then(d => { setLanguages(d); setLoading(false); }).catch(() => setLoading(false));
  }, []);

  return (
    <div className="page home-page">
      <div className="hero">
        <div className="hero-terminal">
          <div className="terminal-dots">
            <span className="dot dot-red"></span>
            <span className="dot dot-yellow"></span>
            <span className="dot dot-green"></span>
            <span className="terminal-title">alam-mo-ah ~ bash</span>
          </div>
          <div className="terminal-body">
            <p className="terminal-line"><span className="t-prompt">➜</span> <span className="t-cmd">whoami</span></p>
            <p className="terminal-output">alam<span className="t-accent">mo</span>ah</p>
            <p className="terminal-line"><span className="t-prompt">➜</span> <span className="t-cmd">cat README.md</span></p>
            <p className="terminal-output hero-tagline">{tagline}</p>
            <p className="terminal-line"><span className="t-prompt">➜</span> <span className="t-cursor">_</span></p>
          </div>
        </div>
        <div className="hero-text">
          <h1 className="hero-title">Alam<br /><span className="hero-title-accent">Mo</span><br />Ah</h1>
          <p className="hero-sub">No-nonsense coding syntax reference. Built for IT students who just need to get things done.</p>
          <button className="hero-cta" onClick={onAuthOpen}>create account →</button>
        </div>
      </div>

      <div className="section-label">// pick your language</div>
      {loading ? (
        <p className="loading-text">fetching languages<span className="blink">...</span></p>
      ) : (
        <div className="lang-grid">
          {languages.map(lang => (
            <Link to={`/language/${lang.id}`} key={lang.id} className="lang-card">
              <div className="lang-card-header">
                <span className="lang-icon" style={{ color: lang.color }}>{lang.icon}</span>
                <div className="lang-card-dot" style={{ background: lang.color }}></div>
              </div>
              <h2 className="lang-name">{lang.name}</h2>
              <p className="lang-desc">{lang.description}</p>
              <div className="lang-stats">
                <span className="stat">{lang.categoryCount} categories</span>
                <span className="stat-sep">·</span>
                <span className="stat">{lang.entryCount} syntaxes</span>
              </div>
              <span className="lang-explore">explore →</span>
            </Link>
          ))}
        </div>
      )}

      <div className="marquee-section">
        <div className="marquee-track">
          {['javascript', 'python', 'java', 'c++', 'sql', 'html', 'css', 'react', 'nodejs', 'spring boot', 'git', 'algorithms'].flatMap((t, i) => [
            <span key={i} className="marquee-item">{t}</span>,
            <span key={`b${i}`} className="marquee-item">{t}</span>
          ])}
        </div>
      </div>
    </div>
  );
}

// ─── LANGUAGE PAGE ────────────────────────────────────────────────────────────
function LanguagePage({ onAuthOpen }) {
  const { id } = useParams();
  const [lang, setLang] = useState(null);
  const [activeCategory, setActiveCategory] = useState(null);
  const [copied, setCopied] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetch(`${API}/languages/${id}`)
      .then(r => r.json())
      .then(d => { setLang(d); setActiveCategory(Object.keys(d.categories)[0]); setLoading(false); })
      .catch(() => setLoading(false));
  }, [id]);

  const copy = (text, key) => {
    navigator.clipboard.writeText(text).then(() => { setCopied(key); setTimeout(() => setCopied(null), 2000); });
  };

  if (loading) return <div className="page"><p className="loading-text">loading<span className="blink">...</span></p></div>;
  if (!lang || lang.error) return <div className="page"><p className="error-text">Language not found.</p></div>;

  const categories = lang.categories;
  const currentEntries = activeCategory ? categories[activeCategory]?.entries || [] : [];

  return (
    <div className="page lang-page">
      <div className="lang-hero" style={{ '--lang-color': lang.color }}>
        <Link to="/" className="back-link">← back</Link>
        <div className="lang-hero-content">
          <span className="lang-hero-icon">{lang.icon}</span>
          <div>
            <h1 className="lang-hero-title">{lang.name}</h1>
            <p className="lang-hero-desc">{lang.description}</p>
          </div>
        </div>
      </div>

      <div className="lang-layout">
        <aside className="lang-sidebar">
          <p className="sidebar-label">// categories</p>
          {Object.entries(categories).map(([catId, cat]) => (
            <button
              key={catId}
              className={`sidebar-btn ${activeCategory === catId ? 'active' : ''}`}
              onClick={() => setActiveCategory(catId)}
              style={{ '--lang-color': lang.color }}
            >
              <span className="sidebar-btn-arrow">{activeCategory === catId ? '▶' : '·'}</span>
              {cat.title}
              <span className="sidebar-count">{cat.entries.length}</span>
            </button>
          ))}
        </aside>

        <main className="lang-content">
          {activeCategory && (
            <>
              <h2 className="content-category-title">
                <span style={{ color: lang.color }}>//</span> {categories[activeCategory].title}
              </h2>
              <div className="entries-list">
                {currentEntries.map(entry => (
                  <div key={entry.id} className="entry-card">
                    <div className="entry-header">
                      <h3 className="entry-title">{entry.title}</h3>
                      <div className="entry-header-right">
                        <div className="entry-tags">
                          {entry.tags.map(tag => <span key={tag} className="entry-tag">{tag}</span>)}
                        </div>
                        <BookmarkButton
                          entry={entry}
                          language={lang}
                          category={categories[activeCategory].title}
                          onAuthRequired={onAuthOpen}
                        />
                      </div>
                    </div>
                    <p className="entry-desc">{entry.description}</p>
                    {entry.usage && (
                      <div className="entry-usage">
                        <span className="usage-label">💡 when to use:</span> {entry.usage}
                      </div>
                    )}
                    <div className="code-block syntax-block">
                      <div className="code-block-header">
                        <span className="code-block-label">syntax</span>
                        <button className="copy-btn" onClick={() => copy(entry.syntax, `${entry.id}-s`)}>
                          {copied === `${entry.id}-s` ? '✓ copied!' : 'copy'}
                        </button>
                      </div>
                      <pre className="code-content"><code>{entry.syntax}</code></pre>
                    </div>
                    <div className="code-block example-block">
                      <div className="code-block-header">
                        <span className="code-block-label">example</span>
                        <button className="copy-btn" onClick={() => copy(entry.example, `${entry.id}-e`)}>
                          {copied === `${entry.id}-e` ? '✓ copied!' : 'copy'}
                        </button>
                      </div>
                      <pre className="code-content"><code>{entry.example}</code></pre>
                    </div>
                  </div>
                ))}
              </div>
            </>
          )}
        </main>
      </div>
    </div>
  );
}

// ─── SEARCH ───────────────────────────────────────────────────────────────────
function SearchPage({ searchQuery, onAuthOpen }) {
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [copied, setCopied] = useState(null);
  const { isLoggedIn } = useAuth();

  useEffect(() => {
    if (!searchQuery.trim()) return;
    setLoading(true);
    fetch(`${API}/search?q=${encodeURIComponent(searchQuery)}`)
      .then(r => r.json()).then(d => { setResults(d); setLoading(false); }).catch(() => setLoading(false));
  }, [searchQuery]);

  const copy = (text, id) => {
    navigator.clipboard.writeText(text).then(() => { setCopied(id); setTimeout(() => setCopied(null), 2000); });
  };

  return (
    <div className="page search-page">
      <div className="search-header">
        <p className="search-query-label">
          <span className="t-prompt">$</span> results for <span className="search-query-term">"{searchQuery}"</span>
        </p>
        {!loading && <p className="search-count">{results.length} result{results.length !== 1 ? 's' : ''} found</p>}
      </div>
      {loading && <p className="loading-text">searching<span className="blink">...</span></p>}
      {!loading && results.length === 0 && searchQuery && (
        <div className="empty-state">
          <p className="empty-icon">¯\_(ツ)_/¯</p>
          <p className="empty-text">Wala akong nakita for "<strong>{searchQuery}</strong>"</p>
          <p className="empty-sub">Try: loop, function, array, class, query, pointer</p>
        </div>
      )}
      <div className="search-results">
        {results.map(entry => (
          <div key={`${entry.languageId}-${entry.id}`} className="entry-card search-result-card">
            <div className="entry-header">
              <div>
                <span className="result-lang-badge" style={{ '--lang-color': entry.languageColor }}>{entry.language}</span>
                <span className="result-cat">{entry.category}</span>
                <h3 className="entry-title">{entry.title}</h3>
              </div>
              <div className="entry-header-right">
                <div className="entry-tags">
                  {entry.tags.map(tag => <span key={tag} className="entry-tag">{tag}</span>)}
                </div>
                <BookmarkButton
                  entry={entry}
                  language={{ id: entry.languageId, name: entry.language, color: entry.languageColor }}
                  category={entry.category}
                  onAuthRequired={onAuthOpen}
                />
              </div>
            </div>
            <p className="entry-desc">{entry.description}</p>
            {entry.usage && <div className="entry-usage"><span className="usage-label">💡</span> {entry.usage}</div>}
            <div className="code-block example-block">
              <div className="code-block-header">
                <span className="code-block-label">syntax</span>
                <button className="copy-btn" onClick={() => copy(entry.syntax, entry.id)}>
                  {copied === entry.id ? '✓ copied!' : 'copy'}
                </button>
              </div>
              <pre className="code-content"><code>{entry.syntax}</code></pre>
            </div>
            <Link to={`/language/${entry.languageId}`} className="view-full-link">view full reference →</Link>
          </div>
        ))}
      </div>
    </div>
  );
}

// ─── ABOUT ────────────────────────────────────────────────────────────────────
function AboutPage() {
  const teamMembers = [
    { name: "Renz Aligado",   role: "Project Team Member" },
    { name: "Yahj Dejarlo",   role: "Team Leader" },
    { name: "Xyron Divina",   role: "Project Team Member" },
    { name: "Gian Sergantes", role: "Project Team Member" },
    { name: "Nigel Lacap",    role: "Project Team Member" },
  ];

  return (
    <div className="about-fullpage">
      {/* Header */}
      <div className="about-hero">
        <h1 className="about-hero-title">About Our Project</h1>
        <p className="about-hero-sub">
          This application was developed collaboratively by the students of <strong>SBIT-1F</strong>,
          a passionate team dedicated to creating an efficient, user-friendly, and innovative solution.
        </p>
      </div>

      {/* Team Cards */}
      <div className="team-grid">
        {teamMembers.map((member, i) => (
          <div key={i} className="team-card">
            <div className="team-avatar">{member.name.charAt(0)}</div>
            <h2 className="team-name">{member.name}</h2>
            <p className="team-role">{member.role}</p>
          </div>
        ))}
      </div>

      {/* Mission */}
      <div className="about-mission">
        <h3 className="mission-title">Our Mission</h3>
        <p className="mission-text">
          Our goal is to build a reliable and modern application that delivers a smooth experience
          for users while showcasing teamwork, creativity, and technical skills.
        </p>
      </div>

      {/* Built with */}
      <div className="about-stack-section">
        <p className="about-stack-label">// built with</p>
        <div className="stack-list">
          {['React', 'Spring Boot', 'Java', 'MySQL', 'JWT'].map(s => (
            <span key={s} className="stack-item">{s}</span>
          ))}
        </div>
      </div>

      <div className="about-footer-note">
        © 2026 All Rights Reserved • Developed by the Project Team
      </div>
    </div>
  );
}

// ─── ROOT ─────────────────────────────────────────────────────────────────────
function AppInner() {
  const [searchQuery, setSearchQuery] = useState('');
  const [authOpen, setAuthOpen] = useState(false);
  const { isLoggedIn } = useAuth();

  return (
    <>
      <Navbar searchQuery={searchQuery} setSearchQuery={setSearchQuery} onAuthOpen={() => setAuthOpen(true)} />
      <div className="app-body">
        <Routes>
          <Route path="/" element={<HomePage onAuthOpen={() => setAuthOpen(true)} />} />
          <Route path="/language/:id" element={<LanguagePage onAuthOpen={() => setAuthOpen(true)} />} />
          <Route path="/search" element={<SearchPage searchQuery={searchQuery} onAuthOpen={() => setAuthOpen(true)} />} />
          <Route path="/library" element={<LibraryPage />} />
          <Route path="/about" element={<AboutPage />} />
        </Routes>
      </div>
      <footer className="footer">
        <p>[ alam<span className="footer-accent">mo</span>ah ] · syntax wiki · para sa mga IT students</p>
      </footer>
      {authOpen && !isLoggedIn && <AuthModal onClose={() => setAuthOpen(false)} />}
    </>
  );
}

export default function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="app">
          <AppInner />
        </div>
      </AuthProvider>
    </Router>
  );
}
