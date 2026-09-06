package com.example.withyou.ui.screens.player

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
    private val userRepository: UserRepository
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

                // -------------------------------------------------
                // Get secure video URL
                // -------------------------------------------------

                val videoUrl =
                    videoAccessRepository
                        .getVideoUrl(videoId)
                        .getOrThrow()

                Log.d(
                    "VideoPlayer",
                    "Video metadata loaded"
                )

                Log.d(
                    "VideoPlayer",
                    "Owner: ${owner?.displayName}"
                )

                Log.d(
                    "VideoPlayer",
                    "Authorized video access"
                )

                // -------------------------------------------------
                // Update UI
                // -------------------------------------------------

                _uiState.value =
                    VideoPlayerUiState(
                        isLoading = false,
                        videoUrl = videoUrl,
                        videoTitle = video.title,
                        videoDescription = video.description,
                        viewCount = video.viewCount,
                        owner = owner
                    )

            } catch (e: Exception) {

                Log.e(
                    "VideoPlayer",
                    "Failed to load video",
                    e
                )

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
                            viewCount =
                                _uiState.value.viewCount + 1
                        )

                    Log.d(
                        "VideoPlayer",
                        "New video view recorded: $videoId"
                    )

                } else {

                    Log.d(
                        "VideoPlayer",
                        "Video already viewed by this user"
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

