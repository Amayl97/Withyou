package com.example.withyou.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)
val items = listOf(
    BottomNavItem(
        title = "Home",
        icon = Icons.Default.Home,
        route = Screen.Home.route
    ),
    BottomNavItem(
        title = "Upload",
        icon = Icons.Default.Add,
        route = Screen.Upload.route
    ),
    BottomNavItem(
        title = "Profile",
        icon = Icons.Default.Person,
        route = Screen.Profile.route
    )
)