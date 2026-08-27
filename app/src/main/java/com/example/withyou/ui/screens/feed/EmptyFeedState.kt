package com.example.withyou.ui.screens.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withyou.ui.theme.AppSpacing

@Composable
fun EmptyFeedState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.Small)
    ) {
        Text(
            text = "No videos yet",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Videos shared with you will appear here.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}