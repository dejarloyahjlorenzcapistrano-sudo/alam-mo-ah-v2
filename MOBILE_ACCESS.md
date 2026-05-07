# 📱 How to Access Alam Mo Ah on Your Phone / Tablet

No app store needed! Since the backend runs on your laptop, any phone or tablet
on the **same WiFi network** can access the site through the browser.

---

## Step 1 — Find your laptop's IP address

Open Command Prompt or PowerShell and type:
```
ipconfig
```
Look for **IPv4 Address** under your WiFi adapter. It will look like:
```
192.168.1.xxx
```

---

## Step 2 — Make sure the backend is running

In IntelliJ, the Spring Boot backend must be running (green ▶ button).

---

## Step 3 — Make sure the frontend is running

In your terminal, `npm start` must be running in the `frontend` folder.

---

## Step 4 — Open on your phone

On your phone's browser, go to:
```
http://192.168.1.xxx:3000
```
Replace `192.168.1.xxx` with your actual IP address from Step 1.

---

## Step 5 — Add to Home Screen (makes it feel like an app!)

**On iPhone:**
1. Open in Safari
2. Tap the Share button (box with arrow)
3. Tap "Add to Home Screen"
4. Tap "Add"

**On Android:**
1. Open in Chrome
2. Tap the 3-dot menu
3. Tap "Add to Home screen"
4. Tap "Add"

It will appear as an icon on your home screen just like a real app! 🎉

---

## ⚠️ Important Notes
- Your laptop must be ON and both servers running for the site to work
- Your phone must be on the **same WiFi** as your laptop
- The login and bookmarks will all work normally from mobile
