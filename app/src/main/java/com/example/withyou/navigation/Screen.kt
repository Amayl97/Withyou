package com.example.withyou.navigation


sealed class Screen(val route: String){
    object Splash : Screen("splash")
    object Contacts : Screen("contacts")
    object Feed : Screen("feed")
    object Home : Screen("home")
    object Login : Screen("login")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Upload : Screen("upload")
    data object Otp : Screen("otp")

}