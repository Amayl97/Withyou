package com.example.withyou.ui.screens.feed

fun formatTimeAgo(createdAt: Long): String {

    val now = System.currentTimeMillis()
    val difference = now - createdAt

    val seconds = difference / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "Recently"

        minutes < 60 -> {
            if (minutes == 1L) "1 min ago"
            else "$minutes mins ago"
        }

        hours < 24 -> {
            if (hours == 1L) "1 hr ago"
            else "$hours hrs ago"
        }

        days < 7 -> {
            if (days == 1L) "1 day ago"
            else "$days days ago"
        }

        else -> {
            val weeks = days / 7

            if (weeks == 1L) "1 week ago"
            else "$weeks weeks ago"
        }
    }
}