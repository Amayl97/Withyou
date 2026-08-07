package com.example.withyou.navigation

import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.withyou.ui.components.BottomNavigationBar
import com.example.withyou.ui.screens.home.HomeScreen
import com.example.withyou.ui.screens.login.LoginScreen
import com.example.withyou.ui.screens.profile.ProfileScreen
import com.example.withyou.ui.screens.splash.SplashScreen
import com.example.withyou.ui.screens.upload.UploadScreen

//Think of navigation as a city map.
//Screens = Places (Home, Login, Profile...)
//Routes = Addresses ("home", "login")
//NavController = GPS
//NavHost = Map
//Scaffold = House layout
//BottomNavigationBar = Menu at the bottom
//BottomNavItem = Information about each menu item


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // Observe the current destination
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Screens that should display the bottom bar
    val bottomBarScreens = listOf(
        Screen.Home.route,
        Screen.Upload.route,
        Screen.Profile.route
    )

    Scaffold(

        bottomBar = {
            if (currentRoute in bottomBarScreens) {
                BottomNavigationBar(navController = navController)
            }
        }

    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route)
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen()
            }

            composable(Screen.Upload.route) {
                UploadScreen()
            }

            composable(Screen.Profile.route) {
                ProfileScreen()
            }

        }

    }
}