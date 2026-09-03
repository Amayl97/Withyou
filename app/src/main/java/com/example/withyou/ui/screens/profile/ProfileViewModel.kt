package com.example.withyou.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.withyou.authentication.data.AuthenticationRepository
import com.example.withyou.data.model.User
import com.example.withyou.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.model.Video
import com.example.withyou.data.repository.VideoRepository
import com.example.withyou.data.repository.VideoStorageRepository
import kotlinx.coroutines.launch

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val videoRepository: VideoRepository,
    private val videoStorageRepository: VideoStorageRepository,
    private val authenticationRepository: AuthenticationRepository
) : ViewModel(){
    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user
    private val _videos =
        mutableStateOf<List<ProfileVideoUiModel>>(emptyList())

    val videos: State<List<ProfileVideoUiModel>> = _videos
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun loadProfile(){
        val uid = authenticationRepository.getCurrentUserId()
        if (uid == null){
            _errorMessage.value = "User not authenticated"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _user.value = userRepository.getUser(uid)
                val videosResult =
                    videoRepository.getUserVideos(uid)

                videosResult
                    .onSuccess { videos ->

                        val profileVideos = videos.map { video ->

                            val thumbnailUrl =
                                video.thumbnailPath?.let { path ->

                                    try {
                                        videoStorageRepository.getSignedThumbnailUrl(path)
                                    } catch (e: Exception) {
                                        null
                                    }
                                }

                            ProfileVideoUiModel(
                                video = video,
                                thumbnailUrl = thumbnailUrl
                            )
                        }

                        _videos.value = profileVideos
                    }
                    .onFailure { exception ->
                        _errorMessage.value =
                            exception.message
                                ?: "Failed to load videos"
                    }

                Log.d(
                    "PROFILE_IMAGE",
                    "Image path = ${_user.value?.profileImagePath}"
                )
            }catch (e: Exception){
                _errorMessage.value = e.message ?: "Failed to load the Profile Image"
            }

            try {
                _user.value = userRepository.getUser(uid)
            }
            catch (e: Exception){
                _errorMessage.value = e.message ?: "Failed to load the Profile"
            }
            finally {
                _isLoading.value= false
            }

        }

    }




}