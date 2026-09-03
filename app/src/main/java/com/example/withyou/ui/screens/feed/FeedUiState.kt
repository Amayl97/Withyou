package com.example.withyou.ui.screens.feed

import com.example.withyou.data.model.Video

data class FeedUiState(
    val videos: List<FeedVideoUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)


