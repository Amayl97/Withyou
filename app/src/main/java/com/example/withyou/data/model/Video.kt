package com.example.withyou.data.model

data class Video(
    val videoId: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val storagePath: String = "",
    val createdAt: Long = 0L
)