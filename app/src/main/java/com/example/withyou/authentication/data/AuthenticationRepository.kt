package com.example.withyou.authentication.data

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.tasks.await

interface AuthenticationRepository{

        fun sendOtp(
    phoneNumber: String,
    activity: Activity,
    onCodeSent: (String) -> Unit,
    onVerificationCompleted: (PhoneAuthCredential) -> Unit,
    onError: (String) -> Unit

    )
    fun verifyOtp(
        verificationId: String,
        otp: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
    fun isUserLoggedIn(): Boolean
    fun logout()
    fun getCurrentUserId(): String?
    fun getCurrentUserPhoneNumber(): String?
    suspend fun getFirebaseIdToken(): String? {
        return FirebaseAuth.getInstance()
            .currentUser
            ?.getIdToken(false)
            ?.await()
            ?.token
    }
}