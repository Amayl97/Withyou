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
import androidx.compose.ui.unit.dp

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(
            modifier = Modifier.height(6.dp)
        )
        Button(
            onClick = onRetry
        ) {
            Text(
                text = "Retry"
            )
        }
    }
}