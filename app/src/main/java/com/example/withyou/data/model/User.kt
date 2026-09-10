package com.example.withyou.data.model

data class User(
    val uid: String = "",
    val username: String = "",
    val displayName: String = "",
    val phoneNumber: String = "",
    val bio: String = "",
    val profileImagePath: String = "",
    val subscriptionStatus: String = "free",
    val subscriptionExpiry: Long? = null
)