package com.example.withyou.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.example.withyou.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withyou.data.model.VideoAnalytics

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Video Analytics")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.error
                            ?: "Unable to load analytics"
                    )
                }
            }

            !uiState.isPremium -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Upgrade to Pro to view analytics"
                    )
                }
            }

            else -> {
                val watchedViewers =
                    uiState.analytics.filter { it.watched }

                val notWatchedViewers =
                    uiState.analytics.filter { !it.watched }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {

                    item {
                        Text(
                            text = "Watched",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                top = 8.dp,
                                bottom = 8.dp
                            )
                        )
                    }

                    if (watchedViewers.isEmpty()) {
                        item {
                            Text(
                                text = "No one has watched this video yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(
                                    vertical = 8.dp
                                )
                            )
                        }
                    } else {
                        items(watchedViewers) { viewer ->
                            AnalyticsViewerRow(
                                viewer = viewer
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Not watched",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                top = 24.dp,
                                bottom = 8.dp
                            )
                        )
                    }

                    if (notWatchedViewers.isEmpty()) {
                        item {
                            Text(
                                text = "No viewers haven't watched the video yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(
                                    vertical = 8.dp
                                )
                            )
                        }
                    } else {
                        items(notWatchedViewers) { viewer ->
                            AnalyticsViewerRow(
                                viewer = viewer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyticsViewerRow(
    viewer: VideoAnalytics
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (viewer.viewerProfileImagePath.isBlank()) {

            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

        } else {

            AsyncImage(
                model = viewer.viewerProfileImagePath,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = viewer.viewerName
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Text(
            text = if (viewer.watched) {
                "✓ Watched"
            } else {
                "○ Not watched"
            }
        )
    }
}