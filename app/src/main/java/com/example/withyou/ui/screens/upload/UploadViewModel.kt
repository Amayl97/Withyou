package com.example.withyou.ui.screens.upload

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.model.Contact
import com.example.withyou.data.model.Video
import com.example.withyou.data.repository.BackendTestRepository
import com.example.withyou.data.repository.ContactsRepository
import com.example.withyou.data.repository.UserRepository
import com.example.withyou.data.repository.VideoRepository
import com.example.withyou.data.repository.VideoStorageRepository
import com.google.firebase.auth.FirebaseAuth
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
    private val videoStorageRepository: VideoStorageRepository,
    private val videoRepository: VideoRepository,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository,
    private val contactsRepository: ContactsRepository,
    private val backendTestRepository: BackendTestRepository
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


    fun onThumbnailSelected(uri: Uri) {

        _uiState.value = _uiState.value.copy(
            selectedThumbnailUri = uri
        )
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
    val contactError =
        if (
            currentState.visibility == "selected_contacts" &&
            currentState.selectedContacts.isEmpty()
        ) {
            "Select at least one contact"
        } else {
            null
        }


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

    val isValid =
        titleError == null &&
                descriptionError == null &&
                contactError == null &&
                currentState.selectedVideoUri != null

    _uiState.value = currentState.copy(
        titleError = titleError,
        descriptionError = descriptionError,
        contactError = contactError,
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
                uploadProgress = 0f,
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
                val allowedContactIds =
                    when (currentState.visibility) {

                        "contacts" -> {
                            getAllContactIds()
                        }

                        "selected_contacts" -> {
                            getAllowedContactIds()
                        }

                        else -> {
                            emptyList()
                        }
                    }
                // 3. Save video metadata to Firestore
                val video = Video(
                    id = videoId,
                    ownerId = ownerId,
                    title = currentState.title.trim(),
                    description = currentState.description.trim(),
                    videoPath = uploadedVideoPath,
                    thumbnailPath = null,
                    visibility = currentState.visibility,
                    allowedContactIds = allowedContactIds,
                    createdAt = System.currentTimeMillis(),
                    duration = currentState.videoInfo?.duration ?: 0L
                )
                videoRepository.saveVideo(video).getOrThrow()

// Upload and metadata save succeeded
                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    uploadProgress = 1f,
                    isReadyForUpload = false,
                    uploadedVideoPath = uploadedVideoPath,
                    uploadError = null
                )

            } catch (e: Exception) {

                Log.e(
                    "UPLOAD_DEBUG",
                    "UPLOAD FAILED",
                    e
                )

                _uiState.value = _uiState.value.copy(
                    isUploading = false,
                    uploadProgress = 0f,
                    isReadyForUpload = true,
                    uploadedVideoPath = null,
                    uploadError = e.message ?: "Video upload failed"
                )
            }
        }
    }

//This prevents retry from starting another upload while one is already running.
    fun retryUpload(contentResolver: ContentResolver) {
        if (_uiState.value.isUploading) {
            return
        }

        uploadVideo(
            contentResolver = contentResolver
        )
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
            selectedContacts = updatedContacts
        )
    }

    private suspend fun getAllowedContactIds(): List<String> {

        val selectedContacts = _uiState.value.selectedContacts



        return selectedContacts.mapNotNull { contact ->

            val user = userRepository.getUserByPhoneNumber(
                contact.phoneNumber
            )

            user?.uid
        }
    }
    private suspend fun getAllContactIds(): List<String> {

        val contacts = contactsRepository.getContact()

        return contacts.mapNotNull { contact ->
            val user = userRepository.getUserByPhoneNumber(
                contact.phoneNumber
            )
            user?.uid
        }
    }

    fun testBackendAuthentication() {
        viewModelScope.launch {
            try {
                val result =
                    backendTestRepository.testAuthentication()

                Log.d(
                    "BACKEND_TEST",
                    result
                )

            } catch (e: Exception) {
                Log.e(
                    "BACKEND_TEST",
                    "Backend test failed",
                    e
                )
            }
        }
    }

    fun testVideoAccess() {
        viewModelScope.launch {
            try {
                val result = backendTestRepository.testVideoAccess(
                    "621902cf-48ce-46af-8dad-e270652b570b"
                )

                Log.d(
                    "VIDEO_ACCESS_TEST",
                    result
                )

            } catch (e: Exception) {
                Log.e(
                    "VIDEO_ACCESS_TEST",
                    "Video access test failed",
                    e
                )
            }
        }
    }

    fun testRealVideoAccess() {
        viewModelScope.launch {
            try {
                val result = backendTestRepository.getVideoAccess(
                    "621902cf-48ce-46af-8dad-e270652b570b"
                )

                Log.d(
                    "REAL_VIDEO_ACCESS",
                    result
                )

            } catch (e: Exception) {
                Log.e(
                    "REAL_VIDEO_ACCESS",
                    "Real video access test failed",
                    e
                )
            }
        }
    }
}