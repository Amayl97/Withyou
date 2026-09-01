package com.example.withyou.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class BackendTestRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    suspend fun testAuthentication(): String =
        withContext(Dispatchers.IO) {

            val user = auth.currentUser
                ?: return@withContext "User is not logged in"

            val token = user
                .getIdToken(false)
                .await()
                .token
                ?: return@withContext "Failed to get Firebase token"

            val url = URL(
                "http://10.0.2.2:3000/test-auth"
            )

            val connection =
                url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"

            connection.setRequestProperty(
                "Authorization",
                "Bearer $token"
            )

            try {
                val responseCode = connection.responseCode

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

                "HTTP $responseCode: $response"

            } finally {
                connection.disconnect()
            }
        }

    suspend fun testVideoAccess(
        videoId: String
    ): String =
        withContext(Dispatchers.IO) {

            val user = auth.currentUser
                ?: return@withContext "User is not logged in"

            val token = user
                .getIdToken(false)
                .await()
                .token
                ?: return@withContext "Failed to get Firebase token"

            val url = URL(
                "http://10.0.2.2:3000/test-video-access/$videoId"
            )

            val connection =
                url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"

            connection.setRequestProperty(
                "Authorization",
                "Bearer $token"
            )

            try {
                val responseCode = connection.responseCode

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

                "HTTP $responseCode: $response"

            } finally {
                connection.disconnect()
            }
        }

    suspend fun getVideoAccess(
        videoId: String
    ): String =
        withContext(Dispatchers.IO) {

            val user = auth.currentUser
                ?: return@withContext "User is not logged in"

            val token = user
                .getIdToken(false)
                .await()
                .token
                ?: return@withContext "Failed to get Firebase token"

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
                val responseCode = connection.responseCode

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

                "HTTP $responseCode: $response"

            } finally {
                connection.disconnect()
            }
        }
}