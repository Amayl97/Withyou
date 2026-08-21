package com.example.withyou.ui.screens.upload

import android.content.ContentResolver
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import jakarta.inject.Inject


//This extracts a frame at approximately 1 second into the video and returns it as a Bitmap.
class VideoThumbnailGenerator @Inject constructor(
    private val contentResolver: ContentResolver
) {

    fun generateThumbnail(uri: Uri): Bitmap? {
        val retriever = MediaMetadataRetriever()

        return try {
            contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->

                retriever.setDataSource(descriptor.fileDescriptor)

                retriever.getFrameAtTime(
                    1_000_000,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                )
            }
        } catch (e: Exception) {
            null
        } finally {
            retriever.release()
        }
    }
}