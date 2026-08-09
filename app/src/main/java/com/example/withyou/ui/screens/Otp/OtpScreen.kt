package com.example.withyou.ui.screens.Otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withyou.authentication.presentation.AuthViewModel
import com.example.withyou.ui.theme.AppSpacing

@Composable
fun OtpScreen(
    onVerificationSuccess: () -> Unit,
    viewModel: AuthViewModel
) {
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
                    onSuccess = onVerificationSuccess
                )
            }
        ) {
            Text("Verify OTP")
        }
    }
}