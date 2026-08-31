package com.example.withyou.ui.screens.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val videoAccessRepository: VideoAccessRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoPlayerUiState())

    val uiState: StateFlow<VideoPlayerUiState> =
        _uiState.asStateFlow()

    fun loadVideo(videoId: String) {

        viewModelScope.launch {

            _uiState.value = VideoPlayerUiState(
                isLoading = true
            )

            val result =
                videoAccessRepository.getVideoUrl(videoId)

            result
                .onSuccess { videoUrl ->

                    Log.d(
                        "VideoPlayer",
                        "Authorized video access"
                    )

                    Log.d(
                        "VideoPlayer",
                        "Video URL received"
                    )

                    _uiState.value =
                        VideoPlayerUiState(
                            isLoading = false,
                            videoUrl = videoUrl
                        )
                }
                .onFailure { exception ->

                    Log.e(
                        "VideoPlayer",
                        "Video access denied/failed",
                        exception
                    )

                    _uiState.value =
                        VideoPlayerUiState(
                            isLoading = false,
                            error = exception.message
                                ?: "Unable to access video"
                        )
                }
        }
    }
}