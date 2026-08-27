package com.example.withyou.ui.screens.feed

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.withyou.R
import com.example.withyou.ui.theme.AppSpacing

@Composable
fun FeedScreen() {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppSpacing.Medium)
    ) {

        // Feed Header
        item {
            Text(
                text = "WithYou",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Videos shared with you",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    top = AppSpacing.ExtraSmall
                )
            )
        }

        // Video 1
        item {
            VideoCard(
                thumbnail = R.drawable.thumbnail,
                title = "A Day in My Life",
                view = "120 views",
                uploadTime = "2 hours ago"
            )
        }

        // Video 2
        item {
            VideoCard(
                thumbnail = R.drawable.thumbnail,
                title = "My University Journey",
                view = "85 views",
                uploadTime = "5 hours ago"
            )
        }

        // Video 3
        item {
            VideoCard(
                thumbnail = R.drawable.thumbnail2,
                title = "Weekend Vlog",
                view = "210 views",
                uploadTime = "Yesterday"
            )
        }
    }
}