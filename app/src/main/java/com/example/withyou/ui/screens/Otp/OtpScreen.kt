package com.example.withyou.ui.screens.Otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.withyou.authentication.presentation.AuthViewModel
import com.example.withyou.ui.theme.AppSpacing

@Composable
fun OtpScreen(
    onExistingUser: () -> Unit,
    onNewUser: () -> Unit,
    viewModel: AuthViewModel
) {
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    var otp by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.Medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Enter OTP",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(AppSpacing.Medium)
        )

        OutlinedTextField(
            value = otp,
            onValueChange = {
                otp = it
            },
            label = {
                Text("OTP")
            }
        )

        Spacer(
            modifier = Modifier.height(AppSpacing.Medium)
        )

        Button(
            onClick = {
                viewModel.verifyOtp(
                    otp = otp,
                    onExistingUser = onExistingUser,
                    onNewUser = onNewUser
                )
            },
            enabled = !isLoading
        ) {
           if(isLoading){
               CircularProgressIndicator()
           }
            else{
                Text("Verify OTP")
           }

        }
        if(errorMessage != null){
            Text(
                text = errorMessage
            )
        }
    }
}