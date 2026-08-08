package com.example.withyou.authentication.presentation

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.withyou.authentication.data.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthenticationRepository
): ViewModel(){
    fun sendOtp(
        phoneNumber: String,
        activity: Activity
    ) {
        repository.sendOtp(
            phoneNumber = phoneNumber,
            activity = activity,

            onCodeSent = { verificationId ->
                // We'll use this for OTP verification
            },

            onVerificationCompleted = { credential ->
                // We'll handle this later
            },

            onError = { error ->
                // We'll handle error state later
            }
        )
    }

    
}
