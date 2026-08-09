package com.example.withyou.authentication.presentation

import android.R.attr.id
import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.withyou.authentication.data.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthenticationRepository,

): ViewModel(){
    private var verificationId: String? = null
    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        onOtpSent: () -> Unit
    ) {
        repository.sendOtp(
            phoneNumber = phoneNumber,
            activity = activity,

            onCodeSent = { id ->
                // We'll use this for OTP verification
                verificationId = id
                onOtpSent()
            },

            onVerificationCompleted = { credential ->
                // We'll handle this later
            },

            onError = { error ->
                // We'll handle error state later
            }
        )
    }


    fun verifyOtp(
        otp: String,
        onSuccess: () -> Unit
    ) {
        Log.d("OTP_VERIFY", "verifyOtp called with OTP: $otp")
        val id = verificationId

        if (id == null) {
            Log.e("OTP_VERIFY", "verificationId is NULL")

            return
        }

        repository.verifyOtp(
            verificationId = id,
            otp = otp,
            onSuccess = {
                onSuccess()
            },
            onError = { error ->
                // We'll handle this properly with UI state later
            }
        )
    }
    
}
