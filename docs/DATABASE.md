# Firestore Database Schema Design

> **Project:** WithYou  
> **Database:** Cloud Firestore (Firebase)

---

# Step 1 — App Features

- Authentication
- Profile
    - Owner Profile (CRUD)
    - Contact Profile (Read Only)
- Contacts
- Upload Video
- Feed
- Comments
- Likes
- Privacy

---

# Step 2 — Screens

## Home Feed

**Needs**

- Videos
- Owner Name
- Owner Profile Photo
- Thumbnail
- Likes Count
- Comments Count

---

## My Profile

**Needs**

- Username
- Bio
- Profile Picture

---

## Contact Profile

**Needs**

- Username
- Bio
- Profile Picture
- Videos that the current user is allowed to watch

---

## Contacts

**Needs**

- Contact Name
- Username
- Profile Picture

---

## Upload / Edit Video

**Needs**

- Video URL
- Thumbnail URL
- Privacy
- Selected Contacts

---

# Step 3 — Entities

- User
- Video
- Comment
- Like
- Contact
- Permission

---

# Step 4 — Entity Definitions

## User

| Field | Type |
|-------|------|
| uid | String |
| username | String |
| displayName | String |
| bio | String |
| profilePhotoURL | String |
| createdAt | Timestamp |

---

## Video

| Field | Type |
|-------|------|
| videoId | String |
| ownerId | String |
| ownerUsername | String |
| ownerDisplayName | String |
| ownerProfilePhotoURL | String |
| title | String |
| description | String (Optional) |
| videoURL | String |
| thumbnailURL | String |
| visibility | String |
| createdAt | Timestamp |
| updatedAt | Timestamp |
| likesCount | Number |
| commentsCount | Number |

---

## Comment

| Field | Type |
|-------|------|
| commentId | String |
| userId | String |
| username | String |
| displayName | String |
| profilePhotoURL | String |
| text | String |
| createdAt | Timestamp |

---

## Like

| Field | Type |
|-------|------|
| userId | String |
| createdAt | Timestamp |

---

## Contact

| Field | Type |
|-------|------|
| contactUserId | String |
| createdAt | Timestamp |

---

## Permission

| Field | Type |
|-------|------|
| userId | String |
| grantedAt | Timestamp |

---

# Step 5 — Entity Relationships

```text
User
│
├── owns → Videos
├── has → Contacts
└── has → Profile

Video
│
├── has → Comments
├── has → Likes
└── has → Permissions
```

---

# Step 6 — Collection vs Subcollection

```text
users (Collection)
│
└── {userId}
    │
    ├── username
    ├── displayName
    ├── bio
    ├── profilePhotoURL
    ├── createdAt
    ├── updatedAt
    │
    └── contacts (Subcollection)
        │
        └── {contactUserId}
            │
            ├── contactUserId
            └── createdAt


videos (Collection)
│
└── {videoId}
    │
    ├── ownerId
    ├── ownerUsername
    ├── ownerDisplayName
    ├── ownerProfilePhotoURL
    │
    ├── title
    ├── description
    ├── videoURL
    ├── thumbnailURL
    │
    ├── visibility
    ├── createdAt
    ├── updatedAt
    ├── likesCount
    ├── commentsCount
    │
    ├── comments (Subcollection)
    │   └── {commentId}
    │
    ├── likes (Subcollection)
    │   └── {userId}
    │
    └── permissions (Subcollection)
        └── {userId}
```

---

# Step 7 — Query Design

## 1. Authentication

### Register / Login

**Purpose**

Create or authenticate a user.

**Collection**

```text
users
```

**Query**

```text
Get user document using Firebase Authentication UID
```

---

## 2. Home Feed

### Purpose

Display videos the current user is allowed to watch.

### Query 1 — Latest Videos

```text
Collection:
videos

ORDER BY createdAt DESC
```

### Query 2 — Visibility Check

For every video:

- **Private**
    - Show only if `ownerId == currentUser.uid`

- **Contacts**
    - Show only if uploader is in the current user's contacts

- **Selected**
    - Check:

```text
videos/{videoId}/permissions/{currentUser.uid}
```

---

## 3. My Videos

```text
Collection:
videos

WHERE ownerId == currentUser.uid

ORDER BY createdAt DESC
```

---

## 4. Upload Video

Create:

```text
videos/{videoId}
```

If visibility is **Selected**:

```text
videos/{videoId}/permissions/{selectedUserUid}
```

---

## 5. Edit Video

Update:

```text
videos/{videoId}
```

Fields:

- title
- description
- thumbnail
- visibility

If selected contacts change:

- Add permission documents
- Remove permission documents

---

## 6. Delete Video

Delete:

```text
videos/{videoId}
```

Also delete:

- Comments
- Likes
- Permissions

---

## 7. My Profile

```text
users/{currentUser.uid}
```

---

## 8. Update Profile

Update:

```text
users/{currentUser.uid}
```

Fields:

- username
- displayName
- bio
- profilePhotoURL

Then update duplicated fields inside videos:

- ownerUsername
- ownerDisplayName
- ownerProfilePhotoURL

---

## 9. Contact Profile

Load profile:

```text
users/{contactUid}
```

Load videos:

```text
videos

WHERE ownerId == contactUid

ORDER BY createdAt DESC
```

Apply visibility rules:

- Private → Owner only
- Contacts → Viewer must be a contact
- Selected → Permission document must exist

---

## 10. Contacts List

```text
users/{currentUser.uid}/contacts
```

Retrieve profile:

```text
users/{contactUid}
```

Display:

- Username
- Display Name
- Profile Photo

---

## 11. Add Contact

```text
Create

users/{currentUser.uid}/contacts/{contactUserId}
```

---

## 12. Remove Contact

```text
Delete

users/{currentUser.uid}/contacts/{contactUserId}
```

---

## 13. Load Comments

```text
videos/{videoId}/comments

ORDER BY createdAt ASC
```

---

## 14. Add Comment

```text
Create

videos/{videoId}/comments/{commentId}
```

Then:

```text
commentsCount++
```

---

## 15. Delete Comment

```text
Delete

videos/{videoId}/comments/{commentId}
```

Then:

```text
commentsCount--
```

---

## 16. Like Video

Check:

```text
videos/{videoId}/likes/{currentUser.uid}
```

If it doesn't exist:

```text
Create Like
```

Then:

```text
likesCount++
```

---

## 17. Unlike Video

```text
Delete

videos/{videoId}/likes/{currentUser.uid}
```

Then:

```text
likesCount--
```

---

# Step 8 — Firestore Security Rules (Logical Design)

## Users Collection

### Read

Any authenticated user can read another user's public profile:

- Username
- Display Name
- Bio
- Profile Photo

### Write

Only the owner can:

- Update profile
- Delete account
- Change username
- Change display name
- Change profile picture
- Change bio

---

## Videos Collection

### Read

A video can be viewed only if one of the following conditions is true:

```text
1. request.auth.uid == ownerId

OR

2. visibility == CONTACTS
   AND viewer exists in owner's contacts

OR

3. visibility == SELECTED
   AND permissions/{viewerUid} exists
```

Otherwise:

❌ Access Denied

---

### Create

Only authenticated users.

```text
ownerId = request.auth.uid
```

The client must never be able to upload a video on behalf of another user.

---

### Update

Only the owner can update:

- Title
- Description
- Thumbnail
- Visibility

---

### Delete

Only the owner can delete the video.

---

## Likes

### Read

If the video is readable, likes are readable.

### Create

- Only authenticated users
- One like per user

```text
likes/{userId}
```

### Delete

Only the user who created the like.

---

## Comments

### Read

If the video is readable, comments are readable.

### Create

Only users who can access the video.

### Delete

Allowed for:

- Comment owner
- Video owner

---

## Permissions

### Read

Only:

- Video owner
- The user whose permission document it is

### Write

Only the video owner.

---

## Contacts

### Read

Only the owner.

### Add

Only the owner.

### Delete

Only the owner.

---

## External Sharing Policy

Videos cannot be shared publicly.

The application enforces:

- ❌ No public share links
- ❌ No copyable video URLs
- ❌ No external access outside WithYou
- ✅ Only the owner may intentionally export or share their own content (future feature)

> **Note:** Firestore Security Rules cannot prevent users from taking screenshots or screen recordings.

---

## Permission Management

Permission documents are created when a video is uploaded with **Selected Contacts** visibility.

Only the video owner may:

- Add permissions
- Remove permissions

Clients must never be allowed to:

- Change `ownerId`
- Grant permissions on behalf of another user

---

# Overall Privacy Model

Every video is private by default.

The owner decides who can view each video through one of the following visibility modes:

- **Private** — Only the owner.
- **Contacts** — All approved contacts.
- **Selected** — Only explicitly permitted contacts.

Videos are never publicly accessible on the internet, and only the owner controls who can view, modify, or share their content.

This privacy-first architecture is one of the core design principles of **WithYou**.