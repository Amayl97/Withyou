package com.example.withyou.data.model

data class Video(
    val id: String = "",
    val ownerId: String = "",
    val title: String = "",
    val description: String = "",
    val videoPath: String = "",
    val thumbnailPath: String? = null,
    val visibility: String = "private",
    val allowedContactIds: List<String> = emptyList(),
    val createdAt: Long = 0L,
    val duration: Long = 0L
)