package com.example.withyou.ui.screens.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withyou.ui.theme.AppSpacing

@Composable
fun FeedLoadingState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSpacing.Large),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}