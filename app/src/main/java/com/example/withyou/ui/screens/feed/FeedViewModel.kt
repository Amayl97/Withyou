package com.example.withyou.ui.screens.feed

import android.util.Log
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

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val videoStorageRepository: VideoStorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())

    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    fun loadVideos() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            try {

                val result = videoRepository.getVideos()

                result
                    .onSuccess { videos ->

                        val feedVideos = videos.map { video ->

                            val thumbnailUrl = video.thumbnailPath?.let { path ->
                                try {
                                    val url = videoStorageRepository.getSignedThumbnailUrl(path)

                                    Log.d(
                                        "THUMBNAIL_DEBUG",
                                        "Signed thumbnail URL generated successfully: $url"
                                    )
                                    url
                                } catch (e: Exception) {
                                    Log.e(
                                        "THUMBNAIL_DEBUG",
                                        "Failed to generate thumbnail URL for: $path",
                                        e
                                    )

                                    null
                                }
                            }

                            FeedVideoUiModel(
                                video = video,
                                thumbnailUrl = thumbnailUrl
                            )
                        }

                        _uiState.value = _uiState.value.copy(
                            videos = feedVideos,
                            isLoading = false,
                            error = null
                        )
                    }
                    .onFailure { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Failed to load videos"
                        )
                    }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                        ?: "Failed to load videos"
                )
            }
        }
    }
}