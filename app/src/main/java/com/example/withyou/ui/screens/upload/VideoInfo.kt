package com.example.withyou.ui.screens.upload

import android.net.Uri

data class VideoInfo(
    val uri: Uri,
    val fileName: String,
    val duration: Long,
    val fileSize: Long,
    val mimeType: String?
)