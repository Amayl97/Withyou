package com.example.withyou.ui.screens.profile

import com.example.withyou.data.model.Video

data class EditVideoUiState(
    val video: Video? = null,
    val title: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)