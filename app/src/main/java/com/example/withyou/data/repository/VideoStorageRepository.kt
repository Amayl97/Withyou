package com.example.withyou.data.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

class VideoStorageRepository @Inject constructor(
    private val supabaseClient: SupabaseClient
) {

    fun generateVideoId(): String {
        return UUID.randomUUID().toString()
    }

    fun createVideoPath(
        userId: String,
        videoId: String
    ): String {
        return "$userId/$videoId.mp4"
    }

    fun createThumbnailPath(
        userId: String,
        videoId: String
    ): String {
        return "$userId/${videoId}_thumbnail.jpg"
    }

    suspend fun uploadVideo(
        contentResolver: ContentResolver,
        videoUri: Uri,
        userId: String,
        videoId: String
    ): String {

        val videoPath = createVideoPath(
            userId = userId,
            videoId = videoId
        )

        val videoBytes = contentResolver
            .openInputStream(videoUri)
            ?.use { inputStream ->

                val outputStream = ByteArrayOutputStream()

                val buffer = ByteArray(8 * 1024)

                var bytesRead: Int

                while (inputStream.read(buffer).also {
                        bytesRead = it
                    } != -1
                ) {
                    outputStream.write(
                        buffer,
                        0,
                        bytesRead
                    )
                }

                outputStream.toByteArray()
            }
            ?: throw IllegalStateException(
                "Unable to read selected video"
            )

        supabaseClient.storage
            .from("videos")
            .upload(
                path = videoPath,
                data = videoBytes
            )

        return videoPath
    }

    suspend fun uploadThumbnail(
        contentResolver: ContentResolver,
        thumbnailUri: Uri,
        userId: String,
        videoId: String
    ): String {

        val thumbnailPath = createThumbnailPath(
            userId = userId,
            videoId = videoId
        )

        val thumbnailBytes = contentResolver
            .openInputStream(thumbnailUri)
            ?.use { inputStream ->

                val outputStream = ByteArrayOutputStream()

                val buffer = ByteArray(8 * 1024)

                var bytesRead: Int

                while (inputStream.read(buffer).also {
                        bytesRead = it
                    } != -1
                ) {
                    outputStream.write(
                        buffer,
                        0,
                        bytesRead
                    )
                }

                outputStream.toByteArray()
            }
            ?: throw IllegalStateException(
                "Unable to read selected thumbnail"
            )

        supabaseClient.storage
            .from("videos")
            .upload(
                path = thumbnailPath,
                data = thumbnailBytes
            )

        return thumbnailPath
    }

    suspend fun getSignedVideoUrl(videoPath: String): String {
        return supabaseClient.storage
            .from("videos")
            .createSignedUrl(
                path = videoPath,
                expiresIn = 30.minutes
            )
    }

    suspend fun getSignedThumbnailUrl(thumbnailPath: String): String {

        Log.d(
            "SUPABASE_AUTH_DEBUG",
            "Requesting signed thumbnail URL"
        )

        return supabaseClient.storage
            .from("videos")
            .createSignedUrl(
                path = thumbnailPath,
                expiresIn = 30.minutes
            )
    }

}