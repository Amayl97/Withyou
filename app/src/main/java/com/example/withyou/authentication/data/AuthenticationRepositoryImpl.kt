package com.example.withyou.authentication.data

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import javax.inject.Inject


class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth
) : AuthenticationRepository{

    override fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onError: (String) -> Unit
    ) {
        TODO("Not yet implemented")
    }

}