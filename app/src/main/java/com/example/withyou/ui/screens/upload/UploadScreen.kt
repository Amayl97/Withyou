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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                thumbnail = uiState.thumbnail,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(MaterialTheme.shapes.large)
            )
        }
        //video metadata
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

        Spacer(modifier = Modifier.height(24.dp))
// title of the video
        uiState.selectedVideoUri?.let {

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.title,
                onValueChange = { viewModel.onTitleChanged(it) },
                label = {
                    Text("Title")
                },
                placeholder = {
                    Text("Enter video title")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                isError = uiState.titleError != null
            )

            uiState.titleError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

// description of the video
        uiState.selectedVideoUri?.let {

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChanged(it) },
                label = {
                    Text("Description")
                },
                placeholder = {
                    Text("Enter video description")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                isError = uiState.descriptionError != null
            )

            uiState.descriptionError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
//continue button
        uiState.selectedVideoUri?.let {

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
                    viewModel.validateForm()
                }
            ) {
                Text("Upload")
            }
            if (uiState.isReadyForUpload) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Video is ready for upload",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

}