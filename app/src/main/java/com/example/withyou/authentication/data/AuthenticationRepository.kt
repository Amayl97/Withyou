package com.example.withyou.authentication.data

import android.app.Activity
import com.google.firebase.auth.PhoneAuthCredential

interface AuthenticationRepository{

        fun sendOtp(
    phoneNumber: String,
    activity: Activity,
    onCodeSent: (String) -> Unit,
    onVerificationCompleted: (PhoneAuthCredential) -> Unit,
    onError: (String) -> Unit

    )
}