package com.example.withyou.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import coil3.compose.AsyncImage
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.withyou.R
import com.example.withyou.ui.theme.AppSpacing
import com.example.withyou.ui.theme.Border
import com.example.withyou.ui.theme.Primary
import com.example.withyou.ui.theme.WhiteBackground

@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    viewModel: EditProfileViewModel
) {
    val user = viewModel.user.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    val selectedImageUri= viewModel.selectedImageUri.value

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        uri ->
        uri?.let { viewModel.selectProfileImage(it) }
    }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    var username by remember {
        mutableStateOf("")
    }

    var bio by remember {
        mutableStateOf("")
    }

    LaunchedEffect(user) {
        user?.let {
            username = it.username
            bio = it.bio
        }
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

        // Profile image
        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(90.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(90.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(
            modifier = Modifier.height(AppSpacing.Medium)
        )

        // Change picture
        Button(
            onClick = {
                // Change Photo
                imagePickerLauncher.launch("image/*")
            },
            border = BorderStroke(
                width = 1.dp,
                color = Border
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = WhiteBackground,
                contentColor = Primary
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Change Picture",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(AppSpacing.Medium)
        )

        // Username
        Text(
            text = "Username:",
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = {
                Text("Username")
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        Spacer(
            modifier = Modifier.height(AppSpacing.Medium)
        )

        // Bio
        Text(
            text = "Bio:",
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = bio,
            onValueChange = {
                bio = it
            },
            label = {
                Text("Bio")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            shape = MaterialTheme.shapes.medium
        )

        Spacer(
            modifier = Modifier.height(AppSpacing.Medium)
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(
                modifier = Modifier.height(AppSpacing.Small)
            )
        }
        // Save button
        Button(
            onClick = {
                viewModel.updateProfile(
                    username = username,
                    bio = bio,
                    onSuccess = {
                        onBack()
                    }
                )
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = WhiteBackground
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = "Save",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

