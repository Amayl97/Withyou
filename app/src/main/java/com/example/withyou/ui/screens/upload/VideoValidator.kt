package com.example.withyou.ui.screens.upload

import jakarta.inject.Inject

class VideoValidator @Inject constructor() {

    companion object {
        private const val MAX_VIDEO_SIZE_BYTES = 100L * 1024 * 1024

        private val SUPPORTED_VIDEO_TYPES = setOf(
            "video/mp4",
            "video/webm",
            "video/3gpp"
        )
    }

    fun validate(videoInfo: VideoInfo): String? {

        if (videoInfo.mimeType !in SUPPORTED_VIDEO_TYPES) {
            return "Unsupported video format."
        }

        if (videoInfo.fileSize > MAX_VIDEO_SIZE_BYTES) {
            return "Video size must be less than 100 MB."
        }

        return null
    }
}