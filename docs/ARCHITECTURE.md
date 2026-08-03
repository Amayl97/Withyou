# WithYou Architecture

> Version: 1.0 (Hackathon MVP)

---

# Overview

**WithYou** is a private video-sharing Android application that allows users to share videos securely with selected contacts. It is built using Kotlin, Jetpack Compose, Firebase, and RevenueCat.

The application follows the **MVVM (Model-View-ViewModel)** architecture with the **Repository Pattern** to ensure scalability, maintainability, and separation of concerns.

---

# MVP Features

- ✅ Phone Authentication
- ✅ User Profile
- ✅ Contacts
- ✅ Upload Video
- ✅ Comments
- ✅ Views
- ✅ Private Video Sharing
- ✅ Feed
- ✅ Video Player
- ✅ Privacy Settings


---

# Future Features

- ⭐ AI Subtitles
- ⭐ Video Analytics
- ⭐ Reactions
- ⭐ Search
- ⭐ Categories
- ⭐ Cloud Backup
- ⭐ Video Download
- ⭐ Video Compression
- ⭐ Dark Theme

---

# Tech Stack

| Area | Technology |
|------|------------|
| Platform | Android |
| Language | Kotlin |
| UI | Jetpack Compose |
| Design | Material 3 |
| Architecture | MVVM |
| Authentication | Firebase Authentication |
| Database | Cloud Firestore |
| Storage | Firebase Storage |
| Video Player | Media3 ExoPlayer |
| Payments | RevenueCat |
| Billing | Google Play Billing |
| Premium | RevenueCat Entitlements |
| Premium MVP Feature | Video Analytics |
| Future Premium Feature | AI Subtitles |
| Image Loading | Coil |
| Async | Coroutines + Flow |
| Local Storage | DataStore |
| Notifications | Firebase Cloud Messaging (FCM) |

---

# Architecture Pattern

The application follows the MVVM architecture.

```
UI (Jetpack Compose)
        │
        ▼
ViewModel
        │
        ▼
Repository
        │
        ▼
Firebase Services
```

No UI component communicates directly with Firebase. All data passes through the Repository layer.

---

# High-Level System Architecture

```
                    WithYou App

       Firebase Authentication
               │
               ▼
            User UID
               │
 ┌─────────────┼──────────────┐
 │             │              │
 ▼             ▼              ▼
Firestore   Firebase Storage  RevenueCat
 │             │              │
 │             │              │
Data      Videos & Images  Premium Status
```

---

# Firebase Architecture

## Cloud Firestore

```
Firestore

├── Users
│     └── uid
│          ├── Profile Information
│          └── Subscription Status
│
├── Videos
│     └── videoId
│          └── Video Metadata
│
├── VideoViewers
│     └── videoId + viewerUid
│          └── Access Permission
│
└── VideoAnalytics
      └── videoId
           └── viewerUid
                ├── Watch Status
                ├── Watch Duration
                └── Completion Status
```

---

## Firebase Storage

```
Firebase Storage

├── profileImages
│     └── {uid}
│           └── profile.jpg
│
└── videos
      └── {videoId}
            ├── video.mp4
            ├── thumbnail.jpg
            └── subtitles
                  ├── en.vtt
                  ├── ko.vtt
                  └── ur.vtt
```

Future versions may also include:

- Preview videos
- AI-generated subtitles
- AI-translated audio
- Metadata cache

---

# Authentication Flow

```
Splash

↓

Phone Number Login

↓

Firebase Authentication

↓

Firebase UID

↓

Users Collection

↓

Home
```

---

# Registration Flow

```
Open App

↓

Phone Authentication

↓

New User?

↓

Create User Document

↓

Upload Profile Picture

↓

Complete Profile

↓

Home
```

---

# Home Feed Flow

```
Open App

↓

Authenticate User

↓

Query VideoViewers

↓

Fetch Accessible Video IDs

↓

Fetch Video Metadata

↓

Load Thumbnails

↓

Display Feed
```

---

# Upload Flow

```
Select Video

↓

Generate Video ID

↓

Upload Video

↓

Generate Thumbnail

↓

Upload Thumbnail

↓

Save Video Metadata

↓

Select Contacts

↓

Grant Access

↓

Upload Complete
```

---

# Contact Selection Flow

```
Read Device Contacts

↓

Compare Phone Numbers

↓

Users Collection

↓

Registered Users
    │
    ├── Can Receive Videos
    │
    └── Share Inside App

↓

Non-Registered Users

↓

Invite via WhatsApp / Instagram
```

---

# Video Watching Flow

```
User Opens App

↓

Query VideoViewers

↓

Retrieve Accessible Videos

↓

Fetch Metadata

↓

Load Thumbnail

↓

Play Video

↓

Update Analytics
```

---

# Premium Flow

```
User Opens Premium

↓

Google Play Billing

↓

RevenueCat

↓

Subscription Successful

↓

Update Firestore

↓

subscriptionStatus = PREMIUM
```

---

# Analytics Access Flow

```
Open Analytics

↓

Check Subscription

↓

Premium?

Yes
    ↓
Show Analytics

No
    ↓
Show RevenueCat Paywall
```

---

# Application Flow

```
Splash

↓

Login

↓

Home

↓

Contacts

↓

Select Contact

↓

Upload Video

↓

Privacy Settings

↓

Upload Complete

↓

Receiver Opens App

↓

Video Appears

↓

Watch Video
```

---

# Screens

## Authentication

- Splash
- Login
- Register

## Main

- Home
- Feed
- Video Player
- Notifications

## Profile

- Profile
- Edit Profile
- Settings

## Sharing

- Contacts
- Add Contact
- Upload Video
- Preview Video
- Privacy Settings

## Premium

- Analytics
- RevenueCat Paywall

---

# Backend Components

```
WithYou Backend

├── Firebase Authentication
│      └── Phone Authentication
│
├── Cloud Firestore
│      ├── Users
│      ├── Videos
│      ├── VideoViewers
│      └── VideoAnalytics
│
└── Firebase Storage
       ├── profileImages
       └── videos
```

---

# Design Principles

- MVVM Architecture
- Repository Pattern
- Single Source of Truth
- Modular Feature Structure
- Firebase as Backend
- Secure Private Video Sharing
- Premium Features Managed by RevenueCat
- Scalable for Future AI Features

---

# Version

**Architecture Version:** 1.0  
**Project:** WithYou  
**Status:** Finalized for Hackathon MVP