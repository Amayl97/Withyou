package com.example.withyou.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FcmTokenRepository @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val userRepository: UserRepository
) {

    suspend fun updateToken(uid: String): Result<Unit> {
        return try {
            val token = firebaseMessaging.token.await()

            userRepository.updateFcmToken(
                uid = uid,
                token = token
            )

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}