# Alam Mo Ah v2 🖥️
> Syntax wiki for IT students — now with Java backend, MySQL, login & bookmarks.

---

## 🛠️ Prerequisites — Install These First

| Tool | Download | Notes |
|------|----------|-------|
| Java JDK 21 | https://adoptium.net | Choose JDK 21 LTS |
| Maven | https://maven.apache.org/download.cgi | Or use IntelliJ's bundled Maven |
| MySQL 8+ | https://dev.mysql.com/downloads/mysql/ | Note your root password |
| IntelliJ IDEA | https://www.jetbrains.com/idea/ | Community edition is free |
| Node.js 18+ | https://nodejs.org | For the React frontend |

---

## 🗄️ Step 1 — Set Up MySQL Database

Open MySQL Workbench or your terminal and run:

```sql
CREATE DATABASE alammoah;
```

That's it — Spring Boot will create all the tables automatically on first run.

---

## ⚙️ Step 2 — Configure Database Password

Open `backend/src/main/resources/application.properties` and replace:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
```

With your actual MySQL root password.

---

## 🧠 Step 3 — Open Backend in IntelliJ

1. Open **IntelliJ IDEA**
2. Click **"Open"** (not "New Project")
3. Navigate to the `backend/` folder and select it
4. IntelliJ will detect the `pom.xml` — click **"Trust Project"**
5. Wait for Maven to download dependencies (bottom progress bar)
6. Once done, open `src/main/java/com/alammoah/AlamMoAhApplication.java`
7. Click the **▶ green Run button** (or press `Shift+F10`)
8. Backend is running at `http://localhost:8080` ✅

---

## ⚛️ Step 4 — Run the React Frontend

Open a terminal, navigate to the `frontend/` folder:

```bash
cd frontend
npm install --legacy-peer-deps
npm start
```

Frontend is running at `http://localhost:3000` ✅

---

## 🌐 How to Use

| URL | Description |
|-----|-------------|
| `http://localhost:3000` | Main website |
| `http://localhost:8080/api/languages` | API — all languages |
| `http://localhost:8080/api/search?q=loop` | API — search |

---

## 📁 Project Structure

```
alam-mo-ah-v2/
├── backend/                        ← Spring Boot (Java)
│   ├── pom.xml                     ← Maven dependencies
│   └── src/main/
│       ├── java/com/alammoah/
│       │   ├── AlamMoAhApplication.java   ← Entry point
│       │   ├── config/
│       │   │   └── SecurityConfig.java    ← JWT + CORS setup
│       │   ├── controller/
│       │   │   ├── AuthController.java    ← /api/auth/*
│       │   │   ├── BookmarkController.java ← /api/bookmarks/*
│       │   │   └── LanguageController.java ← /api/languages/*
│       │   ├── dto/               ← Request/response objects
│       │   ├── entity/            ← User, Bookmark (DB tables)
│       │   ├── repository/        ← Database queries (JPA)
│       │   ├── security/          ← JWT filter & utility
│       │   └── service/           ← Business logic + syntax data
│       └── resources/
│           └── application.properties   ← DB config
│
└── frontend/                       ← React (JavaScript)
    └── src/
        ├── App.js                  ← All pages & components
        ├── App.css                 ← Dark terminal theme
        ├── context/
        │   └── AuthContext.js      ← Login/auth state
        ├── components/
        │   ├── AuthModal.js        ← Login/register popup
        │   └── BookmarkButton.js   ← ☆ Save button on entries
        └── pages/
            └── LibraryPage.js      ← User's saved syntaxes
```

---

## 🔌 API Endpoints

### Public (no login needed)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/languages` | List all languages |
| GET | `/api/languages/{id}` | Get language + syntax entries |
| GET | `/api/search?q=term` | Search across all languages |
| POST | `/api/auth/register` | Create account |
| POST | `/api/auth/login` | Login, get JWT token |

### Protected (requires `Authorization: Bearer <token>`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/bookmarks` | Get user's bookmarks |
| POST | `/api/bookmarks` | Add bookmark |
| DELETE | `/api/bookmarks/{entryId}` | Remove bookmark |
| GET | `/api/bookmarks/check/{entryId}` | Check if bookmarked |

---

## ➕ Adding New Syntax Entries

Open `backend/src/main/java/com/alammoah/service/SyntaxDataService.java`

Find the language you want to add to and add a new `SyntaxEntry`:

```java
new SyntaxEntry(
    "unique-id",           // must be unique across all entries
    "Entry Title",         // shown as heading
    "syntax template",     // the actual syntax pattern
    "What it does.",       // short description
    "When to use it.",     // usage tip (shown with 💡)
    "// code example",     // copy-paste example
    List.of("tag1", "beginner")  // tags for filtering
)
```

Restart the Spring Boot server — changes appear immediately.

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.2 |
| Security | Spring Security + JWT |
| Database | MySQL 8 + Spring Data JPA |
| Frontend | React 18, React Router v6 |
| Build | Maven (backend), npm (frontend) |
| IDE | IntelliJ IDEA |

---

*Built with ❤️ for IT students. Alam mo na 'yan.*
