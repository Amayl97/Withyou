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
    suspend fun deletePermission(
        videoId: String,
        userId: String
    ): Result<Unit> {
        return try {
            firestore
                .collection("videos")
                .document(videoId)
                .collection("permissions")
                .document(userId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun hasPermission(
        videoId: String,
        userId: String
    ): Result<Boolean> {
        return try {
            val snapshot = firestore
                .collection("videos")
                .document(videoId)
                .collection("permissions")
                .document(userId)
                .get()
                .await()

            Result.success(snapshot.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPermissions(
        videoId: String
    ): Result<List<Permission>> {
        return try {
            val snapshot = firestore
                .collection("videos")
                .document(videoId)
                .collection("permissions")
                .get()
                .await()

            Result.success(
                snapshot.toObjects(Permission::class.java)
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun deletePermissionsForVideo(
        videoId: String
    ): Result<Unit> {
        return try {
            val permissionsSnapshot = firestore
                .collection("videos")
                .document(videoId)
                .collection("permissions")
                .get()
                .await()

            for (permissionDocument in permissionsSnapshot.documents) {
                permissionDocument.reference
                    .delete()
                    .await()
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}