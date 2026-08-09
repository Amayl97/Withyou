package com.example.withyou.data.repository

import com.example.withyou.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await




class UserRepository(
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
}