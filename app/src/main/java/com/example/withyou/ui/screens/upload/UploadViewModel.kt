package com.example.withyou.ui.screens.upload

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.model.Contact
import com.example.withyou.data.model.Video
import com.example.withyou.data.repository.VideoRepository
import com.example.withyou.data.repository.VideoStorageRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val videoMetadataReader: VideoMetadataReader,
    private val videoValidator: VideoValidator,
    private val videoThumbnailGenerator: VideoThumbnailGenerator,
    private val videoStorageRepository: VideoStorageRepository,
    private val videoRepository: VideoRepository,
    private val auth: FirebaseAuth
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
                validationError = null,
                uploadError = null,
                uploadedVideoPath = null
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
    contentResolver: ContentResolver
){
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
        contentResolver = contentResolver
    )
    }
    fun uploadVideo(
        contentResolver: ContentResolver
    ){
        val currentState = _uiState.value
        val videoUri = currentState.selectedVideoUri ?: return
        val videoId = UUID.randomUUID().toString()
        val ownerId = auth.currentUser?.uid
            ?: run {
                _uiState.value = _uiState.value.copy(
                    uploadError = "User is not logged in"
                )
                return
            }

        viewModelScope.launch {

            // Upload started
            _uiState.value = _uiState.value.copy(
                isUploading = true,
                uploadError = null,
                uploadedVideoPath = null
            )

            try {

                // 1. Generate unique video ID
                val videoId = videoStorageRepository.generateVideoId()

                // 2. Upload video to Supabase Storage
                val uploadedVideoPath =
                    videoStorageRepository.uploadVideo(
                        contentResolver = contentResolver,
                        videoUri = videoUri,
                        userId = ownerId,
                        videoId = videoId
                    )

                // 3. Save video metadata to Firestore
                val video = Video(
                    id = videoId,
                    ownerId = ownerId,
                    title = currentState.title.trim(),
                    description = currentState.description.trim(),
                    videoPath = uploadedVideoPath,
                    thumbnailPath = null,
                    visibility = currentState.visibility,
                    allowedContactIds = currentState.allowedContactIds,
                    createdAt = System.currentTimeMillis(),
                    duration = currentState.videoInfo?.duration ?: 0L
                )
                videoRepository.saveVideo(video).getOrThrow()

                // 4. Upload and metadata save succeeded
                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    isReadyForUpload = false,
                    uploadedVideoPath = uploadedVideoPath,
                    uploadError = null
                )

            } catch (e: Exception) {

                // 5. Upload failed
                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    isReadyForUpload = true,
                    uploadedVideoPath = null,
                    uploadError = e.message ?: "Video upload failed"
                )
            }
        }
    }

    fun onVisibilityChanged(visibility: String) {
        _uiState.value = _uiState.value.copy(
            visibility = visibility,
            allowedContactIds = if (visibility != "selected_contacts") {
                emptyList()
            } else {
                _uiState.value.allowedContactIds
            }
        )
    }
    fun onContactSelected(contact: Contact) {

        val currentContacts = _uiState.value.selectedContacts

        val updatedContacts =
            if (currentContacts.any { it.id == contact.id }) {
                currentContacts.filter { it.id != contact.id }
            } else {
                currentContacts + contact
            }

        _uiState.value = _uiState.value.copy(
            selectedContacts = updatedContacts,
            allowedContactIds = updatedContacts.map { it.phoneNumber }
        )
    }
}