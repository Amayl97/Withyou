package com.example.withyou.data.util

object TimeUtils {

    fun getTimeAgo(createdAt: Long): String {

        val currentTime = System.currentTimeMillis()

        val difference = currentTime - createdAt

        val seconds = difference / 1000
        val minutes = difference / (1000 * 60)
        val hours = difference / (1000 * 60 * 60)
        val days = difference / (1000 * 60 * 60 * 24)

        return when {

            seconds < 60 -> "Just now"

            minutes < 60 -> {
                "$minutes min ago"
            }

            hours < 24 -> {
                "$hours h ago"
            }

            days == 1L -> {
                "Yesterday"
            }

            days < 7 -> {
                "$days days ago"
            }

            else -> {
                "$days days ago"
            }
        }
    }
}