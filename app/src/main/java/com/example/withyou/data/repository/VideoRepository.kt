package com.example.withyou.data.repository

import com.example.withyou.data.model.Video
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val firestore: FirebaseFirestore
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
}