@echo off
title Alam Mo Ah - Launcher
color 0A

echo.
echo  =============================================
echo    [alammoah] - Syntax Wiki Launcher
echo  =============================================
echo.

REM ── Check if MySQL is running ─────────────────
echo  [1/3] Checking MySQL...
sc query MySQL80 | find "RUNNING" >nul 2>&1
if %errorlevel% neq 0 (
    echo  Starting MySQL...
    net start MySQL80 >nul 2>&1
    timeout /t 3 /nobreak >nul
)
echo  MySQL is ready.
echo.

REM ── Start Spring Boot backend ─────────────────
echo  [2/3] Starting backend (Spring Boot)...
echo  Make sure IntelliJ is running AlamMoAhApplication.java
echo  OR run: cd backend ^&^& mvn spring-boot:run
echo.

REM ── Start React frontend ──────────────────────
echo  [3/3] Starting frontend...
cd /d "%~dp0frontend"
start "Alam Mo Ah Frontend" cmd /k "npm start"

REM ── Wait and open browser ─────────────────────
echo.
echo  Waiting for frontend to start...
timeout /t 8 /nobreak >nul
start "" "http://localhost:3000"

echo.
echo  =============================================
echo    Site is running at http://localhost:3000
echo    Backend API at   http://localhost:8080
echo  =============================================
echo.
echo  Press any key to exit this window...
echo  (Keep the other terminal window open!)
pause >nul
