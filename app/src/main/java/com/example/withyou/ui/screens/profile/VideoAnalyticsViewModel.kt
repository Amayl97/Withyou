package com.example.withyou.ui.screens.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.model.VideoAnalytics
import com.example.withyou.data.repository.PremiumRepository
import com.example.withyou.data.repository.VideoAnalyticsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

data class VideoAnalyticsUiState(
    val isLoading: Boolean = false,
    val isPremium: Boolean = false,
    val analytics: List<VideoAnalytics> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class VideoAnalyticsViewModel @Inject constructor(
    private val premiumRepository: PremiumRepository,
    private val videoAnalyticsRepository: VideoAnalyticsRepository
) : ViewModel() {

    private val _uiState =
        mutableStateOf(VideoAnalyticsUiState())

    val uiState: State<VideoAnalyticsUiState> =
        _uiState

    fun loadAnalytics(videoId: String) {

        viewModelScope.launch {

            _uiState.value =
                VideoAnalyticsUiState(
                    isLoading = true
                )

            try {

                val isPremium =
                    premiumRepository.isPremium()

                if (!isPremium) {

                    _uiState.value =
                        VideoAnalyticsUiState(
                            isPremium = false,
                            isLoading = false
                        )

                    return@launch
                }

                val analytics =
                    videoAnalyticsRepository
                        .getVideoAnalytics(videoId)
                        .getOrThrow()

                _uiState.value =
                    VideoAnalyticsUiState(
                        isLoading = false,
                        isPremium = true,
                        analytics = analytics
                    )

            } catch (e: Exception) {

                _uiState.value =
                    VideoAnalyticsUiState(
                        isLoading = false,
                        isPremium = isPremiumSafe(),
                        error = e.message
                            ?: "Unable to load analytics"
                    )
            }
        }
    }

    private suspend fun isPremiumSafe(): Boolean {
        return try {
            premiumRepository.isPremium()
        } catch (e: Exception) {
            false
        }
    }
}