package com.example.withyou.ui.screens.upload

import android.net.Uri
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.withyou.R
import com.example.withyou.ui.theme.Primary
import com.example.withyou.ui.theme.WhiteBackground

@Composable
fun UploadScreen() {

    var selectedVideoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedVideoUri = uri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.static_image),
            contentDescription = "Upload video",
            modifier = Modifier.clip(MaterialTheme.shapes.extraLarge)
        )

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

        selectedVideoUri?.let { uri ->

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Selected video: $uri"
            )
        }
    }
}