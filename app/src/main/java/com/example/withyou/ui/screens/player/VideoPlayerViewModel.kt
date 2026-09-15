package com.example.withyou.ui.screens.player

import com.example.withyou.data.repository.VideoStorageRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.repository.UserRepository
import com.example.withyou.data.repository.VideoAccessRepository
import com.example.withyou.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val videoAccessRepository: VideoAccessRepository,
    private val userRepository: UserRepository,
    private val videoStorageRepository: VideoStorageRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(VideoPlayerUiState())

    val uiState: StateFlow<VideoPlayerUiState> =
        _uiState.asStateFlow()

    fun loadVideo(videoId: String) {

        viewModelScope.launch {

            _uiState.value =
                VideoPlayerUiState(
                    isLoading = true
                )

            try {

                // -------------------------------------------------
                // Get video metadata
                // -------------------------------------------------

                val video =
                    videoRepository
                        .getVideoById(videoId)
                        .getOrThrow()

                // -------------------------------------------------
                // Get owner profile
                // -------------------------------------------------

                val owner =
                    userRepository.getUser(
                        video.ownerId
                    )
                val ownerProfileImageUrl =
                    if (!owner?.profileImagePath.isNullOrBlank()) {
                        try {
                            videoStorageRepository.getSignedProfileImageUrl(
                                owner!!.profileImagePath
                            )
                        } catch (e: Exception) {
                            Log.e(
                                "VideoPlayer",
                                "Failed to generate owner profile image URL",
                                e
                            )
                            null
                        }
                    } else {
                        null
                    }
                // -------------------------------------------------
                // Get secure video URL
                // -------------------------------------------------

                val videoUrl =
                    videoAccessRepository
                        .getVideoUrl(videoId)
                        .getOrThrow()

                // -------------------------------------------------
                // Update UI
                // -------------------------------------------------

                _uiState.value =
                    VideoPlayerUiState(
                        isLoading = false,
                        videoUrl = videoUrl,
                        videoTitle = video.title,
                        videoDescription = video.description,
                        watched = videoRepository.hasWatchedVideo(videoId),
                        owner = owner,
                        ownerProfileImageUrl = ownerProfileImageUrl
                    )

            } catch (e: Exception) {

                _uiState.value =
                    VideoPlayerUiState(
                        isLoading = false,
                        error = e.message
                            ?: "Unable to load video"
                    )
            }
        }
    }

    fun recordView(videoId: String) {

        viewModelScope.launch {

            val result =
                videoRepository.recordView(videoId)

            if (result.isSuccess) {

                val wasNewView =
                    result.getOrNull() == true

                if (wasNewView) {

                    _uiState.value =
                        _uiState.value.copy(
                            watched = true
                        )


                }

            } else {

                Log.e(
                    "VideoPlayer",
                    "Failed to record video view",
                    result.exceptionOrNull()
                )
            }
        }
    }
}

