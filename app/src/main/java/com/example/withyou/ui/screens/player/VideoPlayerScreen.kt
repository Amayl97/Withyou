package com.example.withyou.ui.screens.player

import android.app.Activity
import android.content.pm.ActivityInfo
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
import androidx.media3.common.VideoSize
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

    val context = LocalContext.current
    val view = LocalView.current
    val activity = context as Activity

    val uiState by viewModel.uiState.collectAsState()

    var isFullscreen by remember {
        mutableStateOf(false)
    }

    var isBuffering by remember {
        mutableStateOf(false)
    }

    var videoWidth by remember {
        mutableStateOf(0)
    }

    var videoHeight by remember {
        mutableStateOf(0)
    }

    val originalOrientation = remember {
        activity.requestedOrientation
    }

    // -------------------------------------------------
    // Load video
    // -------------------------------------------------

    LaunchedEffect(videoId) {
        viewModel.loadVideo(videoId)
    }

    // -------------------------------------------------
    // Fullscreen system bars
    // -------------------------------------------------

    LaunchedEffect(isFullscreen) {

        val controller =
            WindowInsetsControllerCompat(
                activity.window,
                view
            )

        if (isFullscreen) {

            controller.hide(
                WindowInsetsCompat.Type.systemBars()
            )

            controller.systemBarsBehavior =
                WindowInsetsControllerCompat
                    .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        } else {

            controller.show(
                WindowInsetsCompat.Type.systemBars()
            )
        }
    }

    // -------------------------------------------------
    // Cleanup fullscreen state
    // -------------------------------------------------

    DisposableEffect(Unit) {

        onDispose {

            val controller =
                WindowInsetsControllerCompat(
                    activity.window,
                    view
                )

            controller.show(
                WindowInsetsCompat.Type.systemBars()
            )

            activity.requestedOrientation =
                originalOrientation
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
                        horizontalAlignment =
                            Alignment.CenterHorizontally
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

                val player =
                    remember(uiState.videoUrl) {

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

                                        override fun
                                                onVideoSizeChanged(
                                            videoSize: VideoSize
                                        ) {

                                            videoWidth =
                                                videoSize.width

                                            videoHeight =
                                                videoSize.height
                                        }

                                        override fun
                                                onPlaybackStateChanged(
                                            playbackState: Int
                                        ) {

                                            isBuffering =
                                                playbackState ==
                                                        Player.STATE_BUFFERING
                                        }

                                        override fun
                                                onPlayerError(
                                            error:
                                            androidx.media3.common
                                            .PlaybackException
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

                // -------------------------------------------------
                // Video player
                // -------------------------------------------------

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
                                .aspectRatio(
                                    if (
                                        videoWidth > 0 &&
                                        videoHeight > 0
                                    ) {
                                        videoWidth.toFloat() /
                                                videoHeight.toFloat()
                                    } else {
                                        16f / 9f
                                    }
                                )
                        }
                )

                // -------------------------------------------------
                // Buffering indicator
                // -------------------------------------------------

                if (isBuffering) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.align(
                                Alignment.Center
                            )
                    )
                }

                // -------------------------------------------------
                // Release player
                // -------------------------------------------------

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
            modifier =
                Modifier.align(
                    Alignment.TopStart
                )
        ) {

            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        // -------------------------------------------------
        // Fullscreen button
        // -------------------------------------------------

        if (uiState.videoUrl != null) {

            IconButton(
                onClick = {

                    if (!isFullscreen) {

                        // Landscape video
                        if (videoWidth > videoHeight) {

                            activity.requestedOrientation =
                                ActivityInfo
                                    .SCREEN_ORIENTATION_LANDSCAPE

                            // Portrait video
                        } else if (videoHeight > videoWidth) {

                            activity.requestedOrientation =
                                ActivityInfo
                                    .SCREEN_ORIENTATION_PORTRAIT
                        }

                        isFullscreen = true

                    } else {

                        isFullscreen = false

                        activity.requestedOrientation =
                            originalOrientation
                    }
                },

                modifier =
                    Modifier.align(
                        Alignment.TopEnd
                    )
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

