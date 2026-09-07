package com.example.withyou.data.repository

import android.util.Log
import com.example.withyou.data.model.Video
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun saveVideo(video: Video): Result<Unit> {
        return try {
            firestore
                .collection("videos")
                .document(video.id)
                .set(video)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
// for getting videos
suspend fun getVideos(): Result<List<Video>> {
    return try {

        val currentUserId = auth.currentUser?.uid
            ?: return Result.failure(
                IllegalStateException("User is not logged in")
            )

        val snapshot = firestore
            .collection("videos")
            .get()
            .await()

        val videos = snapshot
            .toObjects(Video::class.java)
            .filter { video ->

                when (video.visibility) {

                    "public" -> true

                    "private" -> {
                        video.ownerId == currentUserId
                    }

                    "contacts" -> {
                        video.ownerId == currentUserId ||
                                currentUserId in video.allowedContactIds
                    }

                    "selected_contacts" -> {
                        video.ownerId == currentUserId ||
                                currentUserId in video.allowedContactIds
                    }

                    else -> false
                }
            }
            .map { video ->

                val watched = hasWatchedVideo(video.id)

                video.copy(
                    watched = watched
                )
            }

        val sortedVideos = videos
            .sortedByDescending { it.createdAt }

        Result.success(sortedVideos)

    } catch (e: Exception) {

        Result.failure(e)
    }
}

    suspend fun getUserVideos(
        userId: String
    ): Result<List<Video>> {
        return try {

            val snapshot = firestore
                .collection("videos")
                .whereEqualTo("ownerId", userId)
                .get()
                .await()

            val videos = snapshot
                .toObjects(Video::class.java)
                .sortedByDescending { it.createdAt }

            Result.success(videos)

        } catch (e: Exception) {

            Log.e(
                "VideoRepository",
                "Failed to get user videos",
                e
            )

            Result.failure(e)
        }
    }

    suspend fun getVideoById(videoId: String): Result<Video> {
        return try {
            val snapshot = firestore
                .collection("videos")
                .document(videoId)
                .get()
                .await()

            val video = snapshot.toObject(Video::class.java)
                ?: return Result.failure(
                    IllegalStateException("Video not found")
                )

            Result.success(video)

        } catch (e: Exception) {
            Log.e("VideoRepository", "Failed to get video", e)
            Result.failure(e)
        }
    }

    suspend fun recordView(videoId: String): Result<Boolean> {
        return try {

            val currentUserId = auth.currentUser?.uid
                ?: return Result.failure(
                    IllegalStateException("User is not logged in")
                )

            val viewRef = firestore
                .collection("videos")
                .document(videoId)
                .collection("views")
                .document(currentUserId)

            val viewSnapshot = viewRef.get().await()

            if (viewSnapshot.exists()) {
                return Result.success(false)
            }

            viewRef.set(
                mapOf(
                    "userId" to currentUserId,
                    "viewedAt" to System.currentTimeMillis()
                )
            ).await()

            Result.success(true)

        } catch (e: Exception) {

            Log.e(
                "VideoRepository",
                "Failed to record video view",
                e
            )

            Result.failure(e)
        }
    }

    suspend fun hasWatchedVideo(videoId: String): Boolean {
        return try {

            val currentUserId = auth.currentUser?.uid
                ?: return false

            firestore
                .collection("videos")
                .document(videoId)
                .collection("views")
                .document(currentUserId)
                .get()
                .await()
                .exists()

        } catch (e: Exception) {

            Log.e(
                "VideoRepository",
                "Failed to check video watch status",
                e
            )

            false
        }
    }
}