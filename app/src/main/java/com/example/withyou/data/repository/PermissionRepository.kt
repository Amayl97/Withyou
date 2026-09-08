package com.example.withyou.data.repository

import com.example.withyou.data.model.Permission
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PermissionRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun createPermission(
        permission: Permission
    ): Result<Unit> {
        return try {

            firestore
                .collection("videos")
                .document(permission.videoId)
                .collection("permissions")
                .document(permission.userId)
                .set(permission)
                .await()

            Result.success(Unit)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}