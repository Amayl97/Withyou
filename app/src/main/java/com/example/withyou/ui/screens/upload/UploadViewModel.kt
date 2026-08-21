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
    private val videoValidator: VideoValidator,
    private val videoThumbnailGenerator: VideoThumbnailGenerator
) : ViewModel() {

    private val _uiState = MutableStateFlow(UploadUiState())

    val uiState: StateFlow<UploadUiState> = _uiState.asStateFlow()

    fun onVideoSelected(uri: Uri) {

        val videoInfo = videoMetadataReader.getVideoInfo(uri)

        val validationError = videoValidator.validate(videoInfo)

        if (validationError == null) {

            val thumbnail =
                videoThumbnailGenerator.generateThumbnail(uri)

            _uiState.value = _uiState.value.copy(
                selectedVideoUri = uri,
                videoInfo = videoInfo,
                thumbnail = thumbnail,
                validationError = null
            )

        } else {

            _uiState.value = _uiState.value.copy(
                selectedVideoUri = uri,
                videoInfo = null,
                thumbnail = null,
                validationError = validationError
            )
        }
    }
//to set title
fun onTitleChanged(title: String) {
    _uiState.value = _uiState.value.copy(
        title = title
    )
}
//to set description
fun onDescriptionChanged(description: String) {
    _uiState.value = _uiState.value.copy(
        description = description
    )
}
    fun validateForm() {

        val currentState = _uiState.value

        val titleError = if (currentState.title.trim().isEmpty()) {
            "Title is required"
        } else {
            null
        }

        val descriptionError = if (currentState.description.trim().isEmpty()) {
            "Description is required"
        } else {
            null
        }

        _uiState.value = currentState.copy(
            titleError = titleError,
            descriptionError = descriptionError
        )
    }
}