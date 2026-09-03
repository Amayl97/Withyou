package com.example.withyou.ui.screens.upload

import android.graphics.Bitmap
import android.net.Uri
import com.example.withyou.data.model.Contact

data class UploadUiState(
    val visibility: String = "private",
    val allowedContactIds: List<String> = emptyList(),
    val selectedVideoUri: Uri? = null,
    val videoInfo: VideoInfo? = null,
    val thumbnail: Bitmap? = null,
    val selectedThumbnailUri: Uri? = null,
    val validationError: String? = null,
    val title: String = "",
    val description: String = "",
    val titleError: String? = null,
    val descriptionError: String? = null,
    val isReadyForUpload: Boolean = false,
    val isUploading: Boolean = false,
    val uploadedVideoPath: String? = null,
    val uploadError: String? = null,
    val selectedContacts: List<Contact> = emptyList(),
    val uploadProgress: Float = 0f,
    val contactError: String? = null,
)