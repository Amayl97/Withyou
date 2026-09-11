package com.example.withyou.authentication.presentation

import com.revenuecat.purchases.Offerings
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.repository.PremiumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import android.app.Activity
import com.revenuecat.purchases.Package

data class PremiumUiState(
    val isPremium: Boolean = false,
    val isLoading: Boolean = false,
    val offerings: Offerings? = null,
    val error: String? = null
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

    fun loadOfferings() {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            premiumRepository
                .getOfferings()
                .onSuccess { offerings ->

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            offerings = offerings
                        )
                }
                .onFailure { exception ->

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Unable to load subscription options"
                        )
                }
        }
    }

    fun purchase(
        activity: Activity,
        packageToPurchase: Package
    ) {
        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = true,
                    error = null
                )

            premiumRepository
                .purchase(
                    activity,
                    packageToPurchase
                )
                .onSuccess { isPremium ->

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            isPremium = isPremium
                        )
                }
                .onFailure { exception ->

                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error = exception.message
                                ?: "Purchase failed"
                        )
                }
        }
    }
}