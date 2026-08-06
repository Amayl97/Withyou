package com.example.withyou.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.withyou.ui.screens.home.HomeScreen
import com.example.withyou.ui.screens.login.LoginScreen
import com.example.withyou.ui.screens.splash.SplashScreen
import com.example.withyou.ui.screens.upload.UploadScreen

@Composable
fun AppNavigation() {

    // Controls navigation throughout the app
    val navController = rememberNavController()

    // Navigation graph
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route)
                }
            )
        }

        composable(route = Screen.Home.route) {
            HomeScreen()
        }

    }
}