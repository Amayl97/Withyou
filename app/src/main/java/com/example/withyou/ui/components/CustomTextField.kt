package com.example.withyou.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.pointer.HistoricalChange
import androidx.compose.ui.text.input.TextFieldValue


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        shape = MaterialTheme.shapes.small
    )
}