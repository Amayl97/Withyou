package com.example.withyou.data.repository

import com.example.withyou.data.model.VideoAccessResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoAccessRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    suspend fun getVideoUrl(
        videoId: String
    ): Result<String> =
        withContext(Dispatchers.IO) {

            try {

                val user = auth.currentUser
                    ?: return@withContext Result.failure(
                        Exception("User is not logged in")
                    )

                val token = user
                    .getIdToken(false)
                    .await()
                    .token
                    ?: return@withContext Result.failure(
                        Exception("Failed to get Firebase token")
                    )

                val url = URL(
                    "http://10.0.2.2:3000/videos/$videoId/access"
                )

                val connection =
                    url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"

                connection.setRequestProperty(
                    "Authorization",
                    "Bearer $token"
                )

                try {

                    val responseCode =
                        connection.responseCode

                    val response =
                        if (responseCode in 200..299) {
                            connection.inputStream
                                .bufferedReader()
                                .use { it.readText() }
                        } else {
                            connection.errorStream
                                ?.bufferedReader()
                                ?.use { it.readText() }
                                ?: "Unknown error"
                        }

                    if (responseCode !in 200..299) {
                        return@withContext Result.failure(
                            Exception(
                                "HTTP $responseCode: $response"
                            )
                        )
                    }

                    // We'll parse this properly next.
                    val result =
                        Gson().fromJson(
                            response,
                            VideoAccessResponse::class.java
                        )

                    if (!result.success || result.videoUrl == null) {
                        return@withContext Result.failure(
                            Exception(
                                result.error ?: "Failed to get video URL"
                            )
                        )
                    }

                    Result.success(result.videoUrl)

                } finally {
                    connection.disconnect()
                }

            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}