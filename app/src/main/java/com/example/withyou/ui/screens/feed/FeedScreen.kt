package com.example.withyou.ui.screens.feed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withyou.ui.theme.AppSpacing
import com.example.withyou.R
import com.example.withyou.data.util.TimeUtils

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel()

) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadVideos()
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppSpacing.Medium)
    ) {
        // Feed Header
        item {
            Text(
                text = "WithYou",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Videos shared with you",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    top = AppSpacing.ExtraSmall
                )
            )
        }

        items(uiState.videos) { video ->
            VideoCard(
                thumbnail = R.drawable.thumbnail,
                title = video.title,
                view = "0 views",
                uploadTime = TimeUtils.getTimeAgo(video.createdAt)
            )
        }

    }
}