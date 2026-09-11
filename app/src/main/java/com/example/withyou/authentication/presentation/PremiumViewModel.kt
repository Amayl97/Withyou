package com.example.withyou.authentication.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.repository.PremiumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

data class PremiumUiState(
    val isPremium: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val premiumRepository: PremiumRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(PremiumUiState())
    val uiState: State<PremiumUiState> = _uiState

    fun checkPremiumStatus() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true
            )

            val isPremium = premiumRepository.isPremium()

            _uiState.value = PremiumUiState(
                isPremium = isPremium,
                isLoading = false
            )
        }
    }
}