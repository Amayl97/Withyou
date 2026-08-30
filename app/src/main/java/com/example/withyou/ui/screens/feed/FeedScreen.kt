package com.example.withyou.ui.screens.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withyou.R
import com.example.withyou.data.util.TimeUtils.getTimeAgo
import com.example.withyou.ui.theme.AppSpacing

@Composable
fun FeedScreen(
    onVideoClick: (String) -> Unit,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadVideos()
    }

    when {

        // ⏳ Loading
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // ❌ Error
        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error ?: "Something went wrong"
                )
            }
        }

        // 📭 Empty Feed
        uiState.videos.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No videos shared with you yet"
                )
            }
        }

        // 🎥 Videos Available
        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppSpacing.Medium)
            ) {

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
                        uploadTime = getTimeAgo(video.createdAt),
                        onClick = {
                            onVideoClick(video.id)
                        }
                    )
                }
            }
        }
    }
}