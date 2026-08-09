# WithYou

WithYou is a private video sharing application that allows users to upload, share, and track video engagement.

## Tech Stack

- Kotlin
- Jetpack Compose
- Firebase
- Firestore
- Firebase Storage
- RevenueCat

## Current Phase

Phase 1: Project Setup

## Phase 2 — Foundation

Phase 2 focused on building the core Android application foundation using Kotlin, Jetpack Compose, Firebase, Hilt, and Navigation Compose.

### Completed

- Set up Android project using Kotlin and Jetpack Compose
- Configured project architecture and package structure
- Implemented Navigation Compose
- Added splash, authentication, home, upload, contacts, profile, and settings screens
- Added bottom navigation
- Configured Firebase Authentication
- Implemented phone number authentication and OTP flow
- Added authentication repository and ViewModel
- Configured Hilt dependency injection
- Created the User model
- Implemented Firestore user repository
- Implemented user create, read, and update operations
- Connected the UserViewModel with the repository
- Tested Firestore operations successfully
- Defined storage paths and storage abstraction for future video storage

### Storage Note

Video selection and playback are planned as a local MVP demonstration.

Firebase Cloud Storage integration requires a billing-enabled Firebase project, so the production cloud-storage implementation is deferred.

The intended production storage layer is:

```text
User selects video
        ↓
Firebase Cloud Storage
        ↓
Video URL
        ↓
Firestore video metadata
        ↓
Video playback