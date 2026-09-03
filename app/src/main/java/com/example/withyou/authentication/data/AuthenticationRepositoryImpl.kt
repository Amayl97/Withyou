package com.example.withyou.authentication.data

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth
) : AuthenticationRepository {

    override fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onError: (String) -> Unit
    ) {

        val callbacks =
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(
                    credential: PhoneAuthCredential
                ) {
                    onVerificationCompleted(credential)
                }

                override fun onVerificationFailed(
                    e: FirebaseException
                ) {
                    Log.e("PHONE_AUTH", "Verification failed", e)
                    onError(e.message ?: "Verification failed")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    Log.d("PHONE_AUTH", "OTP sent. Verification ID: $verificationId")
                    onCodeSent(verificationId)
                }
            }

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyOtp(
        verificationId: String,
        otp: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(
            verificationId,
            otp
        )

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(
                        task.exception?.message
                            ?: "OTP verification failed"
                    )
                }
            }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
    override fun getCurrentUserPhoneNumber(): String? {
        return FirebaseAuth.getInstance()
            .currentUser
            ?.phoneNumber
    }
    override suspend fun getFirebaseIdToken(): String? {
        val user = firebaseAuth.currentUser ?: return null

        val tokenResult = user.getIdToken(true).await()

        Log.d(
            "SUPABASE_AUTH_DEBUG",
            "Token claims: ${tokenResult.claims}"
        )

        return tokenResult.token
    }
}