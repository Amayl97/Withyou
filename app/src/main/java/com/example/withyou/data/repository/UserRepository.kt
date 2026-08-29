package com.example.withyou.data.repository

import com.example.withyou.data.model.User
import com.example.withyou.data.util.PhoneNumberUtils
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val usersCollection = firestore.collection("users")

    suspend fun createUser(user: User) {
        usersCollection
            .document(user.uid)
            .set(user)
            .await()
    }

    suspend fun getUser(uid: String): User? {
        val document = usersCollection
            .document(uid)
            .get()
            .await()

        return document.toObject(User::class.java)
    }

    suspend fun updateUser(user: User) {
        usersCollection
            .document(user.uid)
            .set(user)
            .await()
    }

    suspend fun getUserByPhoneNumber(phoneNumber: String): User? {

        val normalizedPhoneNumber =
            PhoneNumberUtils.normalize(phoneNumber)


        val snapshot = usersCollection
            .whereEqualTo(
                "phoneNumber",
                normalizedPhoneNumber
            )
            .limit(1)
            .get()
            .await()

        return snapshot.documents
            .firstOrNull()
            ?.toObject(User::class.java)
    }
}