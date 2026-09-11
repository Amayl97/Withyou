package com.example.withyou.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.Composable

@Composable
fun VideoAnalyticsScreen(
    videoId: String,
    onBack: () -> Unit,
    viewModel: VideoAnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    LaunchedEffect(videoId) {
        viewModel.loadAnalytics(videoId)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error ?: "Unable to load analytics"
                )
            }

            !uiState.isPremium -> {
                Text(
                    text = "Upgrade to Pro to view analytics"
                )
            }

            else -> {
                Text(
                    text = "Analytics: ${uiState.analytics.size} viewers"
                )
            }
        }
    }
}