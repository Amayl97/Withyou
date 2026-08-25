package com.example.withyou.data.repository

import com.example.withyou.data.model.Video
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun saveVideoMetadata(video: Video) {

        firestore
            .collection("videos")
            .document(video.videoId)
            .set(video)
            .await()
    }
}