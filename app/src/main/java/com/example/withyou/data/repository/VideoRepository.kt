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

        Result.success(videos)

    } catch (e: Exception) {
        Result.failure(e)
    }
}
}