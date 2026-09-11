package com.example.withyou.data.repository
import com.example.withyou.data.model.VideoAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VideoAnalyticsRepository @Inject constructor(
    private val permissionRepository: PermissionRepository,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
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

    suspend fun getWatchedViewerIds(
        videoId: String
    ): Result<List<String>> {

        return try {

            val snapshot = firestore
                .collection("videos")
                .document(videoId)
                .collection("views")
                .get()
                .await()

            Result.success(
                snapshot.documents.mapNotNull { document ->
                    document.getString("userId")
                }
            )

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
    suspend fun getVideoAnalytics(
        videoId: String
    ): Result<List<VideoAnalytics>> {

        return try {

            val authorizedViewerIds =
                getAuthorizedViewerIds(videoId)
                    .getOrThrow()

            val watchedViewerIds =
                getWatchedViewerIds(videoId)
                    .getOrThrow()
                    .toSet()

            val analytics = authorizedViewerIds.mapNotNull { viewerId ->

                val user =
                    userRepository.getUser(viewerId)
                        ?: return@mapNotNull null

                VideoAnalytics(
                    viewerId = viewerId,
                    viewerName = user.displayName,
                    viewerProfileImagePath = user.profileImagePath,
                    watched = viewerId in watchedViewerIds
                )
            }

            Result.success(analytics)

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}