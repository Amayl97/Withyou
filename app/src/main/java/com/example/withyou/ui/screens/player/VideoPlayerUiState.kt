package com.example.withyou.ui.screens.player

data class VideoPlayerUiState(
    val isLoading: Boolean = true,
    val videoUrl: String? = null,
    val error: String? = null
)