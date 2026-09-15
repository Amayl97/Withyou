package com.example.withyou.ui.screens.profile

import android.util.Log
import android.content.ContentResolver
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.authentication.data.AuthenticationRepository
import com.example.withyou.data.model.User
import com.example.withyou.data.repository.UserRepository
import com.example.withyou.data.repository.VideoStorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val videoStorageRepository: VideoStorageRepository
) : ViewModel() {

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _selectedImageUri = mutableStateOf<Uri?>(null)
    val selectedImageUri: State<Uri?> = _selectedImageUri

    private val _profileImageUrl = mutableStateOf<String?>(null)
    val profileImageUrl: State<String?> = _profileImageUrl
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun loadProfile() {
        val uid = authenticationRepository.getCurrentUserId()

        if (uid == null) {
            _errorMessage.value = "User not authenticated"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                _user.value = userRepository.getUser(uid)
                val profileImagePath = _user.value?.profileImagePath

                _profileImageUrl.value = null

                if (!profileImagePath.isNullOrBlank()) {
                    try {
                        _profileImageUrl.value =
                            videoStorageRepository.getSignedProfileImageUrl(profileImagePath)
                    } catch (e: Exception) {
                        _profileImageUrl.value = null
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value =
                    e.message ?: "Failed to load the Profile"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectProfileImage(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    private fun validateInput(
        username: String,
        bio: String
    ): String? {

        if (username.isBlank()) {
            return "Username cannot be empty"
        }

        if (username.length < 3 || username.length > 20) {
            return "Username must be between 3 and 20 characters"
        }

        if (!username.matches(Regex("^[a-zA-Z0-9_.]+$"))) {
            return "Username can only contain letters, numbers, _ and ."
        }

        if (bio.length > 150) {
            return "Bio cannot exceed 150 characters"
        }

        return null
    }

    fun updateProfile(
        username: String,
        bio: String,
        contentResolver: ContentResolver,
        onSuccess: () -> Unit
    ) {
        val validationError = validateInput(username, bio)

        if (validationError != null) {
            _errorMessage.value = validationError
            return
        }

        val currentUser = _user.value

        if (currentUser == null) {
            _errorMessage.value = "User not found"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {

                var profileImagePath = currentUser.profileImagePath

                val selectedImage = _selectedImageUri.value

                if (selectedImage != null) {
                    profileImagePath =
                        videoStorageRepository.uploadProfileImage(
                            contentResolver = contentResolver,
                            imageUri = selectedImage,
                            userId = currentUser.uid
                        )
                    Log.d(
                        "PROFILE_IMAGE_DEBUG",
                        "Profile image uploaded successfully: $profileImagePath"
                    )
                }

                val updatedUser = currentUser.copy(
                    username = username,
                    bio = bio,
                    profileImagePath = profileImagePath
                )

                userRepository.updateUser(updatedUser)

                _user.value = updatedUser

                onSuccess()

            } catch (e: Exception) {
                Log.e(
                    "PROFILE_IMAGE_DEBUG",
                    "Profile image upload failed",
                    e
                )

                _errorMessage.value =
                    e.message ?: "Failed to update profile"
            } finally {
                _isLoading.value = false
            }
        }
    }
}