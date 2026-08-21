package com.example.withyou.ui.screens.upload

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withyou.R
import com.example.withyou.ui.theme.Primary
import com.example.withyou.ui.theme.WhiteBackground

@Composable
fun UploadScreen() {

    val viewModel: UploadViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.onVideoSelected(uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (uiState.selectedVideoUri == null) {

            Image(
                painter = painterResource(id = R.drawable.static_image),
                contentDescription = "Upload video",
                modifier = Modifier.clip(MaterialTheme.shapes.extraLarge)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Select a video to share"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose a video from your device"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = WhiteBackground
            ),
            shape = MaterialTheme.shapes.medium,
            onClick = {
                videoPickerLauncher.launch("video/*")
            }
        ) {
            Text("Select Video")
        }
//    video player
        uiState.selectedVideoUri?.let { videoUri ->

            Spacer(modifier = Modifier.height(24.dp))

            VideoPlayer(
                videoUri = videoUri,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(MaterialTheme.shapes.large)
            )
        }
//        Thumbnail of video
        uiState.thumbnail?.let { thumbnail ->

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                bitmap = thumbnail.asImageBitmap(),
                contentDescription = "Video thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(MaterialTheme.shapes.large)
            )
        }
        uiState.videoInfo?.let { videoInfo ->

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "File name: ${videoInfo.fileName}"
            )

            Text(
                text = "Duration: ${videoInfo.duration} ms"
            )

            Text(
                text = "File size: ${videoInfo.fileSize} bytes"
            )

            Text(
                text = "Type: ${videoInfo.mimeType}"
            )
        }
    }
}