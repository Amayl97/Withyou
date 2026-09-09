package com.example.withyou.ui.screens.profile


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withyou.ui.theme.AppSpacing

@Composable
fun EditVideoScreen(
    videoId: String,
    onBack: () -> Unit,
    viewModel: EditVideoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(videoId) {
        viewModel.loadVideo(videoId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.Medium)
    ) {

        // Back button
        IconButton(
            onClick = onBack
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        Spacer(
            modifier = Modifier.height(AppSpacing.Medium)
        )

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            uiState.video != null -> {

                Text(
                    text = "Edit Video",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Medium)
                )

                // Title
                Text(
                    text = "Title:",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Small)
                )

                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = {
                        viewModel.updateTitle(it)
                    },
                    label = {
                        Text("Title")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Medium)
                )

                // Description
                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Small)
                )

                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = {
                        viewModel.updateDescription(it)
                    },
                    label = {
                        Text("Description")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Medium)
                )

                // Privacy
                Text(
                    text = "Privacy: ${uiState.video!!.visibility}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}