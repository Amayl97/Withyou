package com.example.withyou.ui.screens.upload

import android.graphics.Bitmap
import android.net.Uri

data class UploadUiState(
    val selectedVideoUri: Uri? = null,
    val videoInfo: VideoInfo? = null,
    val thumbnail: Bitmap? = null,
    val validationError: String? = null,
    val title: String = "",
    val description: String = "",
    val titleError: String? = null,
    val descriptionError: String? = null
)