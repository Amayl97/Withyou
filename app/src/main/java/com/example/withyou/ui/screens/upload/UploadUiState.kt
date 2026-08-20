package com.example.withyou.ui.screens.upload

import android.net.Uri

data class UploadUiState(
    val selectedVideoUri: Uri? = null,
    val videoInfo: VideoInfo? = null
)