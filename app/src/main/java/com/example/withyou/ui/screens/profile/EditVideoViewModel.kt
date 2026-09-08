package com.example.withyou.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditVideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditVideoUiState())
    val uiState: StateFlow<EditVideoUiState> = _uiState.asStateFlow()
    // We'll add the state and loading logic here next.
    fun loadVideo(videoId: String) {
        viewModelScope.launch {

            _uiState.value = EditVideoUiState(
                isLoading = true
            )

            val result = videoRepository.getVideoById(videoId)

            result
                .onSuccess { video ->
                    _uiState.value = EditVideoUiState(
                        video = video
                    )
                }
                .onFailure { error ->
                    _uiState.value = EditVideoUiState(
                        error = error.message
                    )
                }
        }
    }
}