package com.example.withyou.ui.screens.upload

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.repository.VideoStorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val videoMetadataReader: VideoMetadataReader,
    private val videoValidator: VideoValidator,
    private val videoThumbnailGenerator: VideoThumbnailGenerator,
    private val videoStorageRepository: VideoStorageRepository
) : ViewModel(){

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

//    Validate form
    fun validateAndUpload(
        contentResolver: ContentResolver,
        userId: String
    ) {
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

        val isValid = titleError == null &&
                descriptionError == null &&
                currentState.selectedVideoUri != null

        _uiState.value = currentState.copy(
            titleError = titleError,
            descriptionError = descriptionError,
            isReadyForUpload = isValid
        )

        if (!isValid) {
            return
        }

        uploadVideo(
            contentResolver = contentResolver,
            userId = userId
        )
    }
    fun uploadVideo(
        contentResolver: ContentResolver,
        userId: String
    ) {
        val currentState = _uiState.value
        val videoUri = currentState.selectedVideoUri ?: return

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isUploading = true
            )

            try {
                Log.d("VideoUpload", "Starting upload")
                val videoId = videoStorageRepository.generateVideoId()

                val uploadedVideoPath = videoStorageRepository.uploadVideo(
                    contentResolver = contentResolver,
                    videoUri = videoUri,
                    userId = userId,
                    videoId = videoId
                )
                Log.d("VideoUpload", "Upload successful: $uploadedVideoPath")
                Log.d(
                    "VideoUpload",
                    "Upload successful: $uploadedVideoPath"
                )
                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    uploadedVideoPath = uploadedVideoPath,
                    uploadError = null
                )

            } catch (e: Exception) {
                Log.e(
                    "VideoUpload",
                    "Upload failed",
                    e
                )
                _uiState.value = _uiState.value.copy(
                    isUploading = true,
                    uploadError = null
                )
            }
        }
    }
}