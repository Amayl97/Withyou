package com.example.withyou.data.storage

object StoragePaths {

    fun profileImage(uid: String): String {
        return "users/$uid/profile/profile.jpg"
    }

    fun video(videoId: String): String {
        return "videos/$videoId/video.mp4"
    }

    fun thumbnail(videoId: String): String {
        return "videos/$videoId/thumbnail.jpg"
    }
}