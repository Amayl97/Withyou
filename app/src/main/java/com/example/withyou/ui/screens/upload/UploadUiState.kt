package com.example.withyou.ui.screens.upload

import android.net.Uri

data class UploadUiState(
    val selectedUri: Uri? = null,
    val selectedVideoUri: Uri
)