package com.example.withyou.ui.screens.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val videoMetadataReader: VideoMetadataReader,
    private val videoValidator: VideoValidator
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadUiState())

    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()

    fun onVideoSelected(uri: Uri) {

        val videoInfo = videoMetadataReader.getVideoInfo(uri)

        val validationError = videoValidator.validate(videoInfo)

        _uiState.value = _uiState.value.copy(
            selectedVideoUri = uri,
            videoInfo = if (validationError == null) {
                videoInfo
            } else {
                null
            },
            validationError = validationError
        )
    }
}