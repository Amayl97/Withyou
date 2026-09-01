package com.example.withyou.ui.screens.player

import android.util.Log
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayerScreen(
    videoId: String,
    onBack: () -> Unit,
    viewModel: VideoPlayerViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(videoId) {
        viewModel.loadVideo(videoId)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        when {

            // -------------------------------------------------
            // Loading
            // -------------------------------------------------

            uiState.isLoading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // -------------------------------------------------
            // Error / Access denied
            // -------------------------------------------------

            uiState.error != null -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = uiState.error
                                ?: "Unable to access video"
                        )

                        Button(
                            onClick = onBack
                        ) {
                            Text("Go Back")
                        }
                    }
                }
            }

            // -------------------------------------------------
            // Authorized → Play video
            // -------------------------------------------------

            uiState.videoUrl != null -> {

                val player = remember(uiState.videoUrl) {

                    ExoPlayer.Builder(context)
                        .build()
                        .apply {

                            val mediaItem =
                                MediaItem.fromUri(
                                    uiState.videoUrl!!
                                )

                            setMediaItem(mediaItem)

                            prepare()

                            playWhenReady = true

                            addListener(
                                object : Player.Listener {

                                    override fun onPlayerError(
                                        error: androidx.media3.common.PlaybackException
                                    ) {

                                        Log.e(
                                            "VideoPlayer",
                                            "Playback error: ${error.errorCodeName}",
                                            error
                                        )
                                    }
                                }
                            )
                        }
                }

                AndroidView(
                    factory = {
                        PlayerView(it).apply {

                            this.player = player

                            layoutParams =
                                ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                DisposableEffect(player) {

                    onDispose {
                        player.release()
                    }
                }
            }
        }

        // -------------------------------------------------
        // Back button
        // -------------------------------------------------

        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {

            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
    }
}