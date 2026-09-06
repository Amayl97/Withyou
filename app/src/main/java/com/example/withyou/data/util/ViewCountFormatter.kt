package com.example.withyou.data.util


import java.util.Locale

fun formatViewCount(viewCount: Long): String {
    return when {
        viewCount >= 1_000_000 -> {
            val value = viewCount / 1_000_000.0
            String.format(Locale.US, "%.1fM views", value)
                .replace(".0M", "M")
        }

        viewCount >= 1_000 -> {
            val value = viewCount / 1_000.0
            String.format(Locale.US, "%.1fK views", value)
                .replace(".0K", "K")
        }

        else -> {
            "$viewCount views"
        }
    }
}