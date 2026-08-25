package com.example.withyou.ui.screens.upload

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.withyou.R
import com.example.withyou.ui.screens.contacts.ContactsViewModel
import com.example.withyou.ui.theme.Primary
import com.example.withyou.ui.theme.WhiteBackground
import java.util.jar.Manifest

@Composable
fun UploadScreen() {

    val viewModel: UploadViewModel = hiltViewModel()
    val contactsViewModel: ContactsViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()
    val contactsUiState by contactsViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val contactsPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->

            if (isGranted) {
                contactsViewModel.loadContacts()
            } else {
                contactsViewModel.onPermissionDenied()
            }
        }

    LaunchedEffect(uiState.visibility) {

        if (uiState.visibility == "selected_contacts") {

            val permissionGranted =
                ContextCompat.checkSelfPermission(
                    context,
                    "android.permission.READ_CONTACTS"
                ) == PackageManager.PERMISSION_GRANTED

            if (permissionGranted) {
                contactsViewModel.loadContacts()
            } else {
                contactsPermissionLauncher.launch(
                    "android.permission.READ_CONTACTS"
                )
            }
        }
    }
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

        // Upload image
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

        // Select video button
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

        // Video player
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

        // Video metadata
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

        // Title
        uiState.selectedVideoUri?.let {

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.title,
                onValueChange = {
                    viewModel.onTitleChanged(it)
                },
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

        // Description
        uiState.selectedVideoUri?.let {

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.description,
                onValueChange = {
                    viewModel.onDescriptionChanged(it)
                },
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
            // Privacy
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Who can see this video?",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.visibility == "private",
                        onClick = {
                            viewModel.onVisibilityChanged("private")
                        }
                    )

                    Text("Only me")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.visibility == "contacts",
                        onClick = {
                            viewModel.onVisibilityChanged("contacts")
                        }
                    )

                    Text("My contacts")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.visibility == "selected_contacts",
                        onClick = {
                            viewModel.onVisibilityChanged("selected_contacts")
                        }
                    )

                    Text("Selected contacts")
                }
                if (uiState.visibility == "selected_contacts") {

                    Spacer(modifier = Modifier.height(8.dp))

                    if (contactsUiState.isLoading) {

                        Text(
                            text = "Loading contacts..."
                        )

                    } else if (contactsUiState.error != null) {

                        Text(
                            text = contactsUiState.error!!,
                            color = MaterialTheme.colorScheme.error
                        )

                    } else {

                        contactsUiState.contacts.forEach { contact ->

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Checkbox(
                                    checked = uiState.selectedContacts.any {
                                        it.id == contact.id
                                    },
                                    onCheckedChange = {
                                        viewModel.onContactSelected(contact)
                                    }
                                )

                                Column {
                                    Text(text = contact.name)

                                    Text(
                                        text = contact.phoneNumber,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }



        // Upload button
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
                enabled = !uiState.isUploading,
                onClick = {
                    viewModel.validateAndUpload(
                        contentResolver = context.contentResolver
                    )
                }
            ) {

                if (uiState.isUploading) {
                    Text("Uploading...")
                } else {
                    Text("Upload")
                }
            }

            // Uploading message
            if (uiState.isUploading) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Uploading video...",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Success message
            uiState.uploadedVideoPath?.let {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Video uploaded successfully!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Error message
            uiState.uploadError?.let { error ->

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Upload failed: $error",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
