package com.example.withyou.ui.screens.feed

import com.example.withyou.data.model.Video

data class FeedVideoUiModel(
    val video: Video,
    val thumbnailUrl: String? = null
)