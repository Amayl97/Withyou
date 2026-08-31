package com.example.withyou.data.model


data class VideoAccessResponse(
    val success: Boolean = false,
    val videoUrl: String? = null,
    val error: String? = null
)