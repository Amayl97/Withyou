require("dotenv").config();

const express = require("express");
const cors = require("cors");


const { initializeApp, cert } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");
const { getAuth } = require("firebase-admin/auth");
const serviceAccount = require("./firebase-service-account.json");

const { createClient } = require("@supabase/supabase-js");


initializeApp({
  credential: cert(serviceAccount)
});

const db = getFirestore();
console.log(
  "SUPABASE PROJECT:",
  process.env.SUPABASE_URL
);

const supabase = createClient(
  process.env.SUPABASE_URL,
  process.env.SUPABASE_SERVICE_ROLE_KEY
);

const app = express();

app.use(cors());
app.use(express.json());

async function authenticateUser(req, res, next) {
  try {
    const authHeader = req.headers.authorization;

    if (!authHeader || !authHeader.startsWith("Bearer ")) {
      return res.status(401).json({
        success: false,
        error: "Unauthorized"
      });
    }

    const idToken = authHeader.substring(7);

    const decodedToken = await getAuth().verifyIdToken(idToken);

    req.user = {
      uid: decodedToken.uid
    };

    next();

  } catch (error) {
    console.error("Authentication error:", error);

    return res.status(401).json({
      success: false,
      error: "Invalid authentication token"
    });
  }
}

async function generateVideoUrl(videoPath, res) {
  console.log("========== VIDEO PATH ==========");
  console.log(videoPath);
  console.log("================================");

  const { data, error } = await supabase
    .storage
    .from("videos")
    .createSignedUrl(videoPath, 60 * 60);

  if (error) {
    console.error(
      "Signed URL error:",
      error
    );

    return res.status(500).json({
      success: false,
      error: "Failed to generate video URL"
    });
  }

  return res.json({
    success: true,
    videoUrl: data.signedUrl
  });
}

app.get(
  "/videos/:videoId/access",
  authenticateUser,
  async (req, res) => {
    try {
      const { videoId } = req.params;
      const userId = req.user.uid;

      // Get video metadata from Firestore
      const videoDocument = await db
        .collection("videos")
        .doc(videoId)
        .get();

      if (!videoDocument.exists) {
        return res.status(404).json({
          success: false,
          error: "Video not found"
        });
      }

      const video = videoDocument.data();

      // --------------------------------------------------
      // 1. Owner always has access
      // --------------------------------------------------

      if (video.ownerId === userId) {
        return generateVideoUrl(video.videoPath, res);
      }

      // --------------------------------------------------
      // 2. Private videos are owner-only
      // --------------------------------------------------

      if (video.visibility === "private") {
        return res.status(403).json({
          success: false,
          error: "You do not have permission to view this video"
        });
      }

      // --------------------------------------------------
      // 3. Check allowed contacts
      // --------------------------------------------------

      const allowedContactIds =
        video.allowedContactIds || [];

      if (!allowedContactIds.includes(userId)) {
        return res.status(403).json({
          success: false,
          error: "You do not have permission to view this video"
        });
      }

      // --------------------------------------------------
      // 4. User is authorized
      // --------------------------------------------------

      return generateVideoUrl(video.videoPath, res);

    } catch (error) {
      console.error(
        "Video access error:",
        error
      );

      return res.status(500).json({
        success: false,
        error: "Failed to access video"
      });
    }
  }
);

app.get("/", (req, res) => {
  res.json({
    message: "WithYou backend is running"
  });
});

app.get("/test-firestore", async (req, res) => {
  try {
    const snapshot = await db
      .collection("videos")
      .limit(1)
      .get();

    res.json({
      success: true,
      videoCount: snapshot.size
    });

  } catch (error) {
    console.error(error);

    res.status(500).json({
      success: false,
      error: error.message
    });
  }
});

app.get("/test-video-access/:videoId", authenticateUser, async (req, res) => {
  try {
    const { videoId } = req.params;
    const userId = req.user.uid;

    const videoDocument = await db
      .collection("videos")
      .doc(videoId)
      .get();

    if (!videoDocument.exists) {
      return res.status(404).json({
        success: false,
        error: "Video not found"
      });
    }

    const video = videoDocument.data();

    const allowedContactIds =
      video.allowedContactIds || [];

    const isOwner = video.ownerId === userId;

    const isAllowedContact =
      allowedContactIds.includes(userId);

    res.json({
      success: true,
      userId,
      videoId,
      visibility: video.visibility,
      ownerId: video.ownerId,
      allowedContactIds,
      isOwner,
      isAllowedContact,
      authorized: isOwner || isAllowedContact
    });

  } catch (error) {
    console.error("Test video access error:", error);

    res.status(500).json({
      success: false,
      error: error.message
    });
  }
});
app.get("/test-storage", async (req, res) => {
  try {
    const { data, error } = await supabase
      .storage
      .from("videos")
      .list("sQbwP87PUzLA8LDW6M4tRSVaYLQ2");

    if (error) {
      console.error("Storage list error:", error);

      return res.status(500).json({
        success: false,
        error: error.message
      });
    }

    console.log("STORAGE FILES:", data);

    res.json({
      success: true,
      files: data
    });

  } catch (error) {
    console.error("Storage test error:", error);

    res.status(500).json({
      success: false,
      error: error.message
    });
  }
});
app.get("/test-auth", authenticateUser, (req, res) => {
  res.json({
    success: true,
    uid: req.user.uid
  });
});

const PORT = 3000;

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
