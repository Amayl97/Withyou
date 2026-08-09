package com.example.withyou.authentication.presentation

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.withyou.authentication.data.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthenticationRepository,

): ViewModel(){
    private var verificationId: String? = null
    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    private var _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        onOtpSent: () -> Unit
    ) {
        _isLoading.value = true
        repository.sendOtp(
            phoneNumber = phoneNumber,
            activity = activity,

            onCodeSent = { id ->
                // We'll use this for OTP verification
                verificationId = id
                _isLoading.value = false
                onOtpSent()
            },

            onVerificationCompleted = { credential ->
                // We'll handle this later
            },

            onError = { error ->
                _isLoading.value = false
                _errorMessage.value = error
            }
        )
    }


    fun verifyOtp(
        otp: String,
        onSuccess: () -> Unit
    ) {
        _isLoading.value = true
        Log.d("OTP_VERIFY", "verifyOtp called with OTP: $otp")
        val id = verificationId

        if (id == null) {
            Log.e("OTP_VERIFY", "verificationId is NULL")
            _isLoading.value = false
            _errorMessage.value = "Verification session expired. Please request a new OTP."
            return
        }
        repository.verifyOtp(
            verificationId = id,
            otp = otp,
            onSuccess = {
                _isLoading.value = false
                onSuccess()
            },
            onError = { error ->
                // We'll handle this properly with UI state later
            }
        )
    }
    fun clearError(){
        _errorMessage.value = null
    }
    fun isUserLoggedIn(): Boolean {
        return repository.isUserLoggedIn()
    }
    fun logout() {
        repository.logout()
    }
}
