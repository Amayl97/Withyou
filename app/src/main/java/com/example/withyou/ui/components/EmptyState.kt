package com.example.withyou.ui.components

import android.os.Message
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.dp

@Composable
fun EmptyState(
    message: String,
    actionText: String? = null,
    onActinClick: (() -> Unit)? = null
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )

        if(actionText != null && onActinClick != null){
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Button(
                onClick = onActinClick
            ) {
                Text(actionText)
            }
        }
    }
}