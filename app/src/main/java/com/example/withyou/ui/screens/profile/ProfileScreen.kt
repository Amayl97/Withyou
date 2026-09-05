package com.example.withyou.ui.screens.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import com.example.withyou.data.model.Video
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.withyou.R
import com.example.withyou.ui.theme.AppSpacing
import com.example.withyou.ui.theme.Border
import com.example.withyou.ui.theme.Primary
import com.example.withyou.ui.theme.WhiteBackground
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import coil3.compose.AsyncImage
import com.example.withyou.ui.screens.feed.VideoCard


@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onEditProfile: () -> Unit,
    onContacts: () -> Unit,
    onVideoClick: (String) -> Unit,
    viewModel: ProfileViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    val user = viewModel.user.value
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val videos = viewModel.videos.value
    if (isLoading) {
        LoadingState()
        return
    }
    if (errorMessage != null) {
        ErrorState()
        return
    }

    var menuExpanded by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WhiteBackground)
                .padding(horizontal = AppSpacing.Medium)
                .verticalScroll(rememberScrollState())
        ) {

            // Profile Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppSpacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (user?.profileImagePath.isNullOrBlank()) {



                    Image(
                        painter = painterResource(R.drawable.avatar),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(MaterialTheme.shapes.extraLarge),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    AsyncImage(
                        model = user?.profileImagePath,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(MaterialTheme.shapes.extraLarge),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(
                    modifier = Modifier.height(AppSpacing.Small)
                )

                Text(
                    text = user?.displayName ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(
                    modifier = Modifier.height(AppSpacing.Small)
                )
                Text(
                    text = "@${user?.username ?: ""}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(
                    modifier = Modifier.height(AppSpacing.Small)
                )

                Text(
                    text = user?.bio ?: "",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(
                    modifier = Modifier.height(AppSpacing.Small)
                )

                Button(
                    onClick = {
                        // Navigate to Edit Profile
                        onEditProfile()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = WhiteBackground
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(AppSpacing.Large)
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = Border
            )

            Spacer(
                modifier = Modifier.height(AppSpacing.Medium)
            )

            // Videos Section
            Text(
                text = "My Videos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (videos.isEmpty()) {

                EmptyVideosState()

            } else {

                videos.forEach { item ->

                    VideoCard(
                        video = item.video,
                        thumbnailUrl = item.thumbnailUrl,
                        onClick = {
                            onVideoClick(item.video.id)
                        }
                    )
                }
            }

//            Just for testing
//            LoadingState()
//            ErrorState()
//            EmptyVideosState()

        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(AppSpacing.Small)
        ) {

            IconButton(
                onClick = {
                    menuExpanded = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Profile menu"
                )
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = {
                    menuExpanded = false
                }
            ) {
                DropdownMenuItem(
                    text = {
                        Text("Contacts")
                    },
                    onClick = {
                        menuExpanded = false
                        onContacts()
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text("Logout")
                    },
                    onClick = {
                        menuExpanded = false
                        onLogout()
                    }
                )
            }
        }

    }
}



@Composable
fun VideoCard(
    video: Video,
    thumbnailUrl: String?,
    onClick: () -> Unit
) {
    Spacer(
        modifier = Modifier.height(AppSpacing.Medium)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSpacing.thumbnailHeight)
                .clip(MaterialTheme.shapes.large)
                .background(Color.Black)
        ) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "Video thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(
            modifier = Modifier.height(AppSpacing.Small)
        )

        var menuExpanded by remember {
            mutableStateOf(false)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = video.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Box {

                IconButton(
                    onClick = {
                        menuExpanded = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Video options"
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = {
                        menuExpanded = false
                    }
                ) {

                    DropdownMenuItem(
                        text = {
                            Text("Edit")
                        },
                        onClick = {
                            menuExpanded = false

                            // Edit functionality later
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Delete",
                                color = Color.Red
                            )

                        },
                        onClick = {
                            menuExpanded = false

                            // Delete functionality later
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(AppSpacing.ExtraSmall)
        )

        Text(
            text = "Uploaded video",
            style = MaterialTheme.typography.bodySmall
        )
    }

    Spacer(
        modifier = Modifier.height(AppSpacing.Large)
    )
}

@Composable
fun LoadingState(){
 Box(
     modifier = Modifier.fillMaxSize(),
     contentAlignment = Alignment.Center
 ){
     CircularProgressIndicator()
 }
}

@Composable
fun EmptyVideosState(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No videos yet")
        Text("Upload your first video")
    }
}

@Composable
fun ErrorState(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Something went wrong")
        Text("Unable to load your profile.")

        Button(
            onClick = {

            }
        ) {
            Text("Retry")
        }
    }

}
