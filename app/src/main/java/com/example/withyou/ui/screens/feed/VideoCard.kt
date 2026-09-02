package com.example.withyou.ui.screens.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.withyou.R
import com.example.withyou.data.model.Video
import com.example.withyou.ui.theme.AppSpacing

@Composable
fun VideoCard(
    video: Video,
    onClick: () -> Unit
) {
    Spacer(
        modifier = Modifier.height(AppSpacing.Medium)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {

        Image(
            painter = painterResource(
                id = R.drawable.thumbnail
            ),
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
            text = video.title,
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
                text = "0 views",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "Recently",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    Spacer(
        modifier = Modifier.height(AppSpacing.Large)
    )
}