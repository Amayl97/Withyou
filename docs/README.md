# WithYou

> **Your memories. Your people. Your privacy.**

WithYou is a private video-sharing Android application designed for sharing personal videos with people you actually know.

Most video platforms give you a simple choice: make something public or keep it private. But real-life sharing isn't that simple. Sometimes you want to share a video with all your contacts, sometimes with only a few specific people, and sometimes with no one at all.

**WithYou gives every video its own privacy settings, so you decide exactly who can watch it.**

---

##  Demo & Testing

> **If you are reviewing or running this project, please read this section first.**

### Test Login Accounts

WithYou currently uses **Firebase Phone Authentication in a testing environment**.

To make the project easy to test, the following Firebase test phone numbers can be used to log in:

| # | Test Phone Number | Test OTP |
| - | ----------------- | -------- |
| 1 | `+92 1234567890`   | `012345` |
| 2 | `+92 2345678901`   | `123456` |
| 3 | `+92 3456789012`   | `234567` |
| 4 | `+92 3400302250`   | `234567` |

**Important:** These are Firebase test credentials. Normal SMS verification is not used for these accounts.

If you run the project locally, you will need to configure your own Firebase project and add your own test phone numbers and OTPs.

### Other Testing Notes

* The **`backend` folder must be running** for video playback to work.
* The current maximum video upload size is **50 MB**.
* Firebase and Supabase credentials are **not included** in this repository.
* The project is currently under active development, so some features and UI may change.

---

## 💡 The Problem

We record videos constantly — days out with friends, family gatherings, weddings, trips, and everyday moments.

But sharing those videos can become complicated.

* Sending large videos individually takes time and storage.
* Public platforms aren't appropriate for personal moments.
* Private links can still be inconvenient to manage.
* Not every video is meant for everyone.
* Large wedding and family videos can be difficult to send to relatives living abroad.

For example, in Pakistan, weddings often involve hours of video footage. Family members living abroad may want to watch those memories, but uploading them publicly isn't appropriate because of privacy concerns. Sending one- or two-hour videos individually isn't practical either.

This is the gap WithYou is designed to solve.

---

## 📱 The Solution

WithYou works like a **private video diary for you and your people**.

Upload a video once and decide who can access it.

Each video has its own visibility setting:

| Visibility            | Who can watch?               |
| --------------------- | ---------------------------- |
| **Public**            | All of your contacts         |
| **Selected Contacts** | Only the contacts you choose |
| **Private**           | Only you                     |

So you could share one video with all your friends, another with only your family, and keep another completely private.

**Every video gets its own audience.**

---

## 🔐 Core Feature — Per-Video Privacy

Privacy is the central idea behind WithYou.

Instead of having one privacy setting for your entire account, **each video can have its own access rules**.

For example:

```text
Video 1 → All Contacts
Video 2 → Selected friends(selected contacts)
Video 3 → Family Members(selected contacts)
Video 4 → Only Me
```

This makes WithYou different from simply uploading videos to a public platform or sending large files individually.

---

## ✨ Features

### 🔐 Custom Privacy for Every Video

Choose the audience for each individual video.

### 👥 Contact-Based Sharing

Share videos with people you actually know rather than posting them to a completely open audience.

### 🎥 Video Uploading

Upload personal videos and share them without sending the entire file individually to every person.

**Current limit: 50 MB per video.**

### 📱 Personalized Feed

View videos that have been shared with you.

Unauthorized videos are not displayed in your feed.

### 👤 Profile

View your own uploaded videos and manage your personal collection.

### ▶️ Video Player

Watch videos using a dedicated Media3/ExoPlayer-based player with:

* Portrait playback
* Landscape playback
* Expanded/fullscreen viewing
* Video title and description
* Video owner information

### 🔒 Access Control

Users can only access videos they are authorized to watch.

Private videos remain accessible only to their owner.

---

## 🌎 Why WithYou?

WithYou isn't trying to replace YouTube or other public video platforms.

It's designed for a different kind of sharing:

> **The videos that aren't meant for the internet — just for your people.**

It can be useful for:

* 🎥 Videos from a day out with friends
* 💍 Wedding footage
* 👨‍👩‍👧 Family memories
* 🌎 Sharing memories with relatives living abroad
* 📔 Personal video diaries
* 🔒 Moments that you don't want to post publicly

---

## 🛠️ Tech Stack

### Android

* **Kotlin**
* **Jetpack Compose**
* **Material 3**
* **Navigation**
* **Hilt** — Dependency Injection
* **MVVM** — Application architecture
* **Media3 / ExoPlayer** — Video playback

### Backend & Services

* **Firebase Authentication** — Phone authentication
* **Cloud Firestore** — User and video metadata
* **Supabase Storage** — Video file storage
* **Custom Backend** — Secure video playback support

---

## 🏗️ Architecture

WithYou follows an **MVVM architecture** with Hilt for dependency injection.

```text
┌──────────────────────────┐
│       Jetpack Compose    │
│            UI            │
└────────────┬─────────────┘
             │
             ↓
┌──────────────────────────┐
│       ViewModels         │
│      UI State & Logic    │
└────────────┬─────────────┘
             │
             ↓
┌──────────────────────────┐
│       Repositories       │
│     Data Operations      │
└───────┬──────────┬───────┘
        │          │
        ↓          ↓
   Firebase     Supabase
   Firestore    Storage
        │
        ↓
     Backend
        │
        ↓
 Video Playback
```

---

## 🔒 Privacy Model

The visibility of each video determines who can access it.

```text
                         ┌─────────────┐
                         │    Video    │
                         └──────┬──────┘
                                │
                ┌───────────────┼───────────────┐
                ↓               ↓               ↓
             PUBLIC          SELECTED        PRIVATE
                ↓            CONTACTS            ↓
          All Contacts       Chosen Users       Owner
```

WithYou applies access control when determining which videos a user can see.

This means an unauthorized video should not appear in the user's feed or profile.

---

## Getting Started

### Prerequisites

You will need:

* Android Studio
* A compatible JDK
* Android device or emulator
* Firebase project
* Supabase project
* Backend environment

### 1. Clone the Repository

```bash
git clone https://github.com/Amayl97/Withyou.git
```

Open the project in Android Studio and allow Gradle to sync.

### 2. Configure Firebase

Create your own Firebase project and configure:

* Firebase Authentication
* Phone Authentication
* Cloud Firestore

Add the required Firebase configuration file to the Android project.

Because the project currently uses Firebase's testing environment, configure your own **test phone numbers and OTPs** if you want to run the project with your own Firebase project.

### 3. Configure Supabase

Create a Supabase project and configure the required storage.

WithYou currently uses a bucket named:

```text
videos
```

Configure the required Supabase credentials for your local environment.

### 4. Run the Backend

The repository contains a **`backend`** folder that is required for video playback.

Start the backend before running the Android application.

```text
WithYou/
├── app/
├── backend/
└── ...
```

**If the backend is not running, uploaded videos may not play correctly.**

### 5. Run the Android App

Open the project in Android Studio, connect an Android device or start an emulator, and run the application.

### Upload Limitation

The current maximum upload size is:

**50 MB per video**

---

##  Project Structure

```text
WithYou/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/withyou/
│           │   ├── data/
│           │   ├── di/
│           │   ├── model/
│           │   ├── navigation/
│           │   ├── repository/
│           │   ├── ui/
│           │   └── viewmodel/
│           │
│           └── res/
│
├── backend/
│
├── firebase.json
├── build.gradle.kts
└── README.md
```

---

##  Project Status

WithYou is currently under active development.

### Implemented

* [x] Firebase authentication
* [x] Video uploading
* [x] Video metadata
* [x] Personalized video feed
* [x] User profiles
* [x] Video playback
* [x] Portrait/landscape playback
* [x] Fullscreen video experience
* [x] Contact-based sharing
* [x] Per-video privacy settings
* [x] Selected-contact access control
* [x] Private videos

### Planned / Future Improvements

* [ ] Phone authentication outside the Firebase testing environment
* [ ] Improved contact management
* [ ] Video thumbnails
* [ ] Watched/unwatched states
* [ ] Additional privacy and sharing improvements
* [ ] Further UI/UX refinement

---

##  Design Philosophy

WithYou is built around one idea:

> **Not every memory is for everyone.**

The goal isn't to make sharing more public.

It's to make private sharing **simple, controlled, and personal**.

---

##  Author

**Amayl**

Software Engineering Student & Android Developer

GitHub: [Amayl97](https://github.com/Amayl97)

---

## 📄 License

WithYou is licensed under the **MIT License**.

See the [LICENSE](LICENSE) file for the complete license text.
