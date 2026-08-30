package com.example.withyou.ui.screens.player

import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayerScreen(
    videoId: String,
    onBack: () -> Unit,
    viewModel: VideoPlayerViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(videoId) {
        viewModel.loadVideo(videoId)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (uiState.videoUrl != null) {

            val context = androidx.compose.ui.platform.LocalContext.current

            val player = ExoPlayer.Builder(context).build()
            player.addListener(
                object : androidx.media3.common.Player.Listener {

                    override fun onPlayerError(
                        error: androidx.media3.common.PlaybackException
                    ) {
                        android.util.Log.e(
                            "VideoPlayer",
                            "Playback error: ${error.errorCodeName}",
                            error
                        )
                    }
                }
            )

            val mediaItem = MediaItem.fromUri(
                uiState.videoUrl!!
            )

            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true

            AndroidView(
                factory = {
                    PlayerView(it).apply {
                        this.player = player
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            DisposableEffect(Unit) {
                onDispose {
                    player.release()
                }
            }

        } else if (uiState.isLoading) {

            CircularProgressIndicator()
        }
    }
}