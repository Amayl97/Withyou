package com.example.withyou.ui.screens.upload

import android.content.ContentResolver
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import jakarta.inject.Inject

class VideoMetadataReader @Inject constructor(
    private val contentResolver: ContentResolver
) {

    fun getVideoInfo(uri: Uri): VideoInfo {

        val fileName = getFileName(uri)
        val fileSize = getFileSize(uri)
        val mimeType = contentResolver.getType(uri)

        val retriever = MediaMetadataRetriever()

        return try {
            contentResolver.openFileDescriptor(uri, "r")?.use { parcelFileDescriptor ->

                retriever.setDataSource(parcelFileDescriptor.fileDescriptor)

                val duration = retriever
                    .extractMetadata(
                        MediaMetadataRetriever.METADATA_KEY_DURATION
                    )
                    ?.toLongOrNull() ?: 0L

                VideoInfo(
                    uri = uri,
                    fileName = fileName,
                    duration = duration,
                    fileSize = fileSize,
                    mimeType = mimeType
                )
            } ?: throw IllegalArgumentException("Unable to open video")

        } finally {
            retriever.release()
        }
    }

    private fun getFileName(uri: Uri): String {

        var fileName = "Unknown"

        contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->

            if (cursor.moveToFirst()) {

                val nameIndex =
                    cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)

                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex)
                }
            }
        }

        return fileName
    }

    private fun getFileSize(uri: Uri): Long {

        var fileSize = 0L

        contentResolver.query(
            uri,
            arrayOf(OpenableColumns.SIZE),
            null,
            null,
            null
        )?.use { cursor ->

            if (cursor.moveToFirst()) {

                val sizeIndex =
                    cursor.getColumnIndex(OpenableColumns.SIZE)

                if (sizeIndex != -1) {
                    fileSize = cursor.getLong(sizeIndex)
                }
            }
        }

        return fileSize
    }
}