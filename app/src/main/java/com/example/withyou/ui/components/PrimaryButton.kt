package com.example.withyou.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
){
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.small
        ) {
        Text(
            text = text
        )
    }
}


