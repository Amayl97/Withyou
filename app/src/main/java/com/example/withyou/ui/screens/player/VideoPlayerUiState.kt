package com.example.withyou.ui.screens.player

import com.example.withyou.data.model.User

data class VideoPlayerUiState(
    val isLoading: Boolean = true,
    val videoUrl: String? = null,
    val videoTitle: String = "",
    val videoDescription: String = "",
    val viewCount: Long = 0L,
    val owner: User? = null,
    val error: String? = null
)

