package com.example.withyou.ui.screens.player

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    videoId: String,
    onBack: () -> Unit,
    viewModel: VideoPlayerViewModel = hiltViewModel()
) {
    val view = LocalView.current

    var isFullscreen by remember {
        mutableStateOf(false)
    }
    var isBuffering by remember {
        mutableStateOf(false)
    }
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(videoId) {
        viewModel.loadVideo(videoId)
    }

    LaunchedEffect(isFullscreen) {

        val window = (view.context as Activity).window

        val controller =
            WindowInsetsControllerCompat(window, view)

        if (isFullscreen) {

            controller.hide(
                WindowInsetsCompat.Type.systemBars()
            )

            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        } else {

            controller.show(
                WindowInsetsCompat.Type.systemBars()
            )
        }
    }

    DisposableEffect(Unit) {

        onDispose {

            val window =
                (view.context as Activity).window

            WindowInsetsControllerCompat(
                window,
                view
            ).show(
                WindowInsetsCompat.Type.systemBars()
            )
        }
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

                                    override fun onPlaybackStateChanged(
                                        playbackState: Int
                                    ) {

                                        isBuffering =
                                            playbackState == Player.STATE_BUFFERING
                                    }

                                    override fun onPlayerError(
                                        error: androidx.media3.common.PlaybackException
                                    ) {

                                        isBuffering = false

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

                            useController = true
                            controllerAutoShow = true
                            controllerHideOnTouch = true

                            setShowFastForwardButton(true)
                            setShowRewindButton(true)

                            layoutParams =
                                ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                        }
                    },
                    modifier =
                        if (isFullscreen) {
                            Modifier.fillMaxSize()
                        } else {
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                        }
                )

                if (isBuffering) {

                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

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

        if (uiState.videoUrl != null) {

            IconButton(
                onClick = {
                    isFullscreen = !isFullscreen
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {

                Icon(
                    imageVector =
                        if (isFullscreen) {
                            Icons.Default.FullscreenExit
                        } else {
                            Icons.Default.Fullscreen
                        },
                    contentDescription =
                        if (isFullscreen) {
                            "Exit fullscreen"
                        } else {
                            "Enter fullscreen"
                        }
                )
            }
        }
    }
}