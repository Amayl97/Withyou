package com.example.withyou.ui.screens.login


import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import com.example.withyou.ui.theme.AppSpacing
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withyou.authentication.presentation.AuthViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(AppSpacing.Medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
       Text(
           text = "Login",
           style = MaterialTheme.typography.bodyLarge
       )
        Spacer(modifier = Modifier.height(AppSpacing.Medium))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text("Enter number")
            },
            label = {
                Text("Phone Number")
            }
        )
        Spacer(
            modifier = Modifier.padding(AppSpacing.Medium)
        )
        Button(
            onClick = {}
        ) {
            Text("Send OTP")
        }
    }

}