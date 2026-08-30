package com.example.withyou.ui.screens.player


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.repository.VideoRepository
import com.example.withyou.data.repository.VideoStorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class VideoPlayerUiState(
    val isLoading: Boolean = true,
    val videoUrl: String? = null,
    val error: String? = null
)

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val videoStorageRepository: VideoStorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoPlayerUiState())
    val uiState: StateFlow<VideoPlayerUiState> =
        _uiState.asStateFlow()

    fun loadVideo(videoId: String) {

        viewModelScope.launch {

            _uiState.value = VideoPlayerUiState(
                isLoading = true
            )

            val result = videoRepository.getVideoById(videoId)

            result
                .onSuccess { video ->

                    val videoUrl =
                        videoStorageRepository.getVideoUrl(
                            video.videoPath
                        )

                    _uiState.value = VideoPlayerUiState(
                        isLoading = false,
                        videoUrl = videoUrl
                    )
                }
                .onFailure { exception ->

                    _uiState.value = VideoPlayerUiState(
                        isLoading = false,
                        error = exception.message
                            ?: "Failed to load video"
                    )
                }
        }
    }
}