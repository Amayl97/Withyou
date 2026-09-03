package com.example.withyou.ui.screens.profile

import com.example.withyou.data.model.Video

data class ProfileVideoUiModel(
    val video: Video,
    val thumbnailUrl: String? = null
)