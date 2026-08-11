package com.example.withyou.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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



@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onEditProfile: () -> Unit,
    onContacts: () -> Unit
) {
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

                Image(
                    painter = painterResource(R.drawable.avatar),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Small)
                )

                Text(
                    text = "Amayl Tariq",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "@amayl_Ot7",
                    style = MaterialTheme.typography.bodyMedium,
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.ExtraSmall)
                )

                Text(
                    text = "Bantan Sonyeondan ✨🌸",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Medium)
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
            VideoCard(
                thumbnail = R.drawable.thumbnail,
                title = "A day in my life",
                view = "20 views",
                uploadTime = "2 days ago",
            )

            VideoCard(
                thumbnail = R.drawable.thumbnail2,
                title = "Living my dream day",
                view = "24 views",
                uploadTime = "24 hours",
            )


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
    thumbnail: Int,
    title: String,
    view: String,
    uploadTime: String
){
    Spacer(
        modifier = Modifier.height(AppSpacing.Medium)
    )

    // Video Card
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Image(
            painter = painterResource(thumbnail),
            contentDescription = "Video thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(AppSpacing.thumbnailHeight)
                .clip(MaterialTheme.shapes.large)
        )

        Spacer(
            modifier = Modifier.height(AppSpacing.Small)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(AppSpacing.ExtraSmall)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = view,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = uploadTime,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    Spacer(
        modifier = Modifier.height(AppSpacing.Large)
    )

}

