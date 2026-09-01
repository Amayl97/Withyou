package com.example.withyou.ui.screens.player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
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
import coil3.compose.AsyncImage
import com.example.withyou.R

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

    var playbackError by remember {
        mutableStateOf<String?>(null)
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
    // Cleanup
    // -------------------------------------------------

    DisposableEffect(Unit) {

        onDispose {

            WindowInsetsControllerCompat(
                activity.window,
                view
            ).show(
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
            // Error
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
                            text =
                                uiState.error
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
            // Authorized video
            // -------------------------------------------------

            uiState.videoUrl != null -> {

                val player =
                    remember(uiState.videoUrl) {

                        ExoPlayer.Builder(context)
                            .build()
                            .apply {

                                setMediaItem(
                                    MediaItem.fromUri(
                                        uiState.videoUrl!!
                                    )
                                )

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
                                        override fun onPlaybackStateChanged(
                                            playbackState: Int
                                        ) {
                                            when (playbackState) {

                                                Player.STATE_BUFFERING -> {
                                                    isBuffering = true

                                                    Log.d(
                                                        "VideoPlayer",
                                                        "Video is buffering"
                                                    )
                                                }

                                                Player.STATE_READY -> {
                                                    isBuffering = false
                                                    playbackError = null

                                                    Log.d(
                                                        "VideoPlayer",
                                                        "Video is ready"
                                                    )
                                                }

                                                Player.STATE_ENDED -> {
                                                    isBuffering = false

                                                    Log.d(
                                                        "VideoPlayer",
                                                        "Video playback ended"
                                                    )
                                                }
                                            }
                                        }

                                        override fun onPlayerError(
                                            error: androidx.media3.common.PlaybackException
                                        ) {

                                            isBuffering = false

                                            playbackError =
                                                "Unable to play this video. Please try again."

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
                // Scrollable player content
                // -------------------------------------------------

                Column(
                    modifier =
                        if (isFullscreen) {

                            Modifier.fillMaxSize()

                        } else {

                            Modifier
                                .fillMaxSize()
                                .verticalScroll(
                                    rememberScrollState()
                                )
                        }
                ) {

                    // -------------------------------------------------
                    // Video
                    // -------------------------------------------------

                    Box(
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
                    ) {

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
                                Modifier.fillMaxSize()
                        )

                        // -------------------------------------------------
                        // Buffering
                        // -------------------------------------------------

                        if (isBuffering) {

                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.White
                            )
                        }

                        if (playbackError != null) {

                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = playbackError
                                        ?: "Unable to play video",
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Spacer(
                                    modifier = Modifier.height(12.dp)
                                )

                                Button(
                                    onClick = {
                                        playbackError = null
                                        isBuffering = true

                                        player.seekTo(0)
                                        player.prepare()
                                        player.playWhenReady = true
                                    }
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                        // -------------------------------------------------
                        // Fullscreen button
                        // -------------------------------------------------

                        IconButton(
                            onClick = {

                                if (!isFullscreen) {

                                    if (
                                        videoWidth > videoHeight
                                    ) {

                                        activity.requestedOrientation =
                                            ActivityInfo
                                                .SCREEN_ORIENTATION_LANDSCAPE

                                    } else if (
                                        videoHeight > videoWidth
                                    ) {

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
                                    },
                                tint = Color.White
                            )
                        }
                    }

                    // -------------------------------------------------
                    // Video information
                    // -------------------------------------------------

                    if (!isFullscreen) {

                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp
                                    )
                        ) {

                            // -------------------------------------------------
                            // Title
                            // -------------------------------------------------

                            Text(
                                text = uiState.videoTitle,
                                style =
                                    MaterialTheme.typography
                                        .titleLarge
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            // -------------------------------------------------
                            // Owner
                            // -------------------------------------------------

                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                if (
                                    uiState.owner
                                        ?.profileImagePath
                                        .isNullOrBlank()
                                ) {

                                    Image(
                                        painter =
                                            androidx.compose.ui.res
                                                .painterResource(
                                                    R.drawable.avatar
                                                ),

                                        contentDescription =
                                            "Profile picture",

                                        modifier =
                                            Modifier
                                                .size(44.dp)
                                                .clip(
                                                    CircleShape
                                                ),

                                        contentScale =
                                            ContentScale.Crop
                                    )

                                } else {

                                    AsyncImage(
                                        model =
                                            uiState.owner
                                                ?.profileImagePath,

                                        contentDescription =
                                            "Profile picture",

                                        modifier =
                                            Modifier
                                                .size(44.dp)
                                                .clip(
                                                    CircleShape
                                                ),

                                        contentScale =
                                            ContentScale.Crop
                                    )
                                }

                                Spacer(
                                    modifier =
                                        Modifier.size(12.dp)
                                )

                                Text(
                                    text =
                                        uiState.owner
                                            ?.displayName
                                            ?: "Unknown user",

                                    style =
                                        MaterialTheme.typography
                                            .titleMedium
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(20.dp)
                            )

                            // -------------------------------------------------
                            // Description
                            // -------------------------------------------------

                            Text(
                                text = "Description",
                                style =
                                    MaterialTheme.typography
                                        .titleMedium
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )

                            Text(
                                text =
                                    uiState.videoDescription
                                        .ifBlank {
                                            "No description"
                                        },

                                style =
                                    MaterialTheme.typography
                                        .bodyMedium
                            )
                        }
                    }
                }

                // -------------------------------------------------
                // Back button
                // -------------------------------------------------

                IconButton(
                    onClick = {

                        if (isFullscreen) {

                            isFullscreen = false

                            activity.requestedOrientation =
                                originalOrientation

                        } else {

                            onBack()
                        }
                    },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {

                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
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
    }
}

