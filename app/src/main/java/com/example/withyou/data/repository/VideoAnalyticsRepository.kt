package com.example.withyou.data.repository

import javax.inject.Inject

class VideoAnalyticsRepository @Inject constructor(
    private val permissionRepository: PermissionRepository
) {

    suspend fun getAuthorizedViewerIds(
        videoId: String
    ): Result<List<String>> {

        return try {

            val permissions =
                permissionRepository
                    .getPermissions(videoId)
                    .getOrThrow()

            Result.success(
                permissions.map { it.userId }
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}