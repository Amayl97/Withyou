package com.example.withyou.data.repository

import android.net.Uri

interface StorageRepository {

    suspend fun uploadVideo(
        videoId: String,
        videoUri: Uri
    ): String

    suspend fun uploadThumbnail(
        videoId: String,
        thumbnailUri: Uri
    ): String

    suspend fun uploadProfileImage(
        uid: String,
        imageUri: Uri
    ): String
}