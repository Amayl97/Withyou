# WithYou Navigation Graph

## Purpose

This document defines the navigation structure and screen hierarchy for the WithYou MVP. The goal is to keep navigation simple, intuitive, and scalable as new features are added.

---

# Navigation Flow

```text
                    Splash
                       │
        ┌──────────────┴──────────────┐
        │                             │
Already Authenticated         Not Authenticated
        │                             │
        ▼                             ▼
      Home                         Login
        ▲                             │
        └──────────────┬──────────────┘
                       │
                       ▼
                     Home
                       │
             Bottom Navigation
        ┌────────┬─────────┬─────────┐
        ▼        ▼         ▼
      Home    Upload    Profile
                           │
                           ▼
                        Settings
                           │
        ┌──────────┬──────────┬──────────┬──────────┐
        ▼          ▼          ▼          ▼
    Contacts  Subscription  Privacy    About
                           │
                           ▼
                         Logout
```

---

# Screen Responsibilities

## Splash

**Purpose**

The application's entry point.

**Responsibilities**

* Display application logo.
* Perform initialization tasks.
* Check Firebase authentication state.
* Navigate to **Home** if the user is already authenticated.
* Navigate to **Login** if authentication is required.

---

## Login

**Purpose**

Authenticate the user.

**Responsibilities**

* Phone number authentication.
* OTP verification.
* Handle authentication errors.
* Navigate to **Home** after successful login.

---

## Home

**Purpose**

The primary screen of the application.

**Responsibilities**

* Display videos shared with the user.
* Allow users to browse available content.
* Serve as the default destination after authentication.

---

## Upload

**Purpose**

Allow users to upload videos.

**Responsibilities**

* Select a video.
* Enter video details.
* Configure privacy settings.
* Upload video to Firebase Storage.

---

## Profile

**Purpose**

Display and manage the user's profile.

**Responsibilities**

* Display profile information.
* Edit profile details.
* Open Settings.

---

## Settings

**Purpose**

Provide access to application management features.

**Responsibilities**

* Manage Contacts.
* Manage Subscription.
* Privacy settings.
* About application.
* Logout.

---

## Contacts

**Purpose**

Manage trusted contacts.

**Responsibilities**

* View contacts.
* Add contacts.
* Remove contacts.
* Search contacts.

---

# Bottom Navigation

The Bottom Navigation provides quick access to the application's primary features.

## Visible On

* Home
* Upload
* Profile

## Hidden On

* Splash
* Login
* Settings
* Contacts

---

# Navigation Principles

* Authentication is verified only once when the application launches.
* Users should not be required to log in every time they open the app.
* Bottom Navigation contains only: Home, Upload, Profile.
* Management-related screens (Contacts, Subscription, Privacy, About, Logout) are grouped under **Settings** to keep the primary navigation clean and focused.
* All navigation routes are defined using centralized route constants to improve maintainability and reduce hardcoded strings.

---

# Future Navigation

The following screens may be added in future versions:

* AI Subtitles
* Analytics Dashboard
* Comments
* Search
* Categories
* Notifications
* Cloud Backup
* Download Manager
* Dark Theme
* Language Settings

These features will be integrated without changing the core navigation architecture.
