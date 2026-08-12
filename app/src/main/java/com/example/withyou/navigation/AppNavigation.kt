package com.example.withyou.navigation

import android.annotation.SuppressLint


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.withyou.authentication.presentation.AuthViewModel
import com.example.withyou.ui.components.BottomNavigationBar
import com.example.withyou.ui.screens.home.HomeScreen
import com.example.withyou.ui.screens.login.LoginScreen
import com.example.withyou.ui.screens.Otp.OtpScreen
import com.example.withyou.ui.screens.contacts.ContactsScreen
import com.example.withyou.ui.screens.profile.EditProfileScreen
import com.example.withyou.ui.screens.profile.EditProfileViewModel
import com.example.withyou.ui.screens.profile.ProfileScreen
import com.example.withyou.ui.screens.profile.ProfileViewModel
import com.example.withyou.ui.screens.registration.CompleteProfileScreen
import com.example.withyou.ui.screens.registration.CompleteProfileViewModel
import com.example.withyou.ui.screens.splash.SplashScreen
import com.example.withyou.ui.screens.upload.UploadScreen

// Think of navigation as a city map.
// Screens = Places (Home, Login, Profile...)
// Routes = Addresses ("home", "login")
// NavController = GPS
// NavHost = Map
// Scaffold = House layout
// BottomNavigationBar = Menu at the bottom
// BottomNavItem = Information about each menu item

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    // Observe the current destination
    val navBackStackEntry =
        navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry.value?.destination?.route

    // Screens that should display the bottom bar
    val bottomBarScreens = listOf(
        Screen.Home.route,
        Screen.Upload.route,
        Screen.Profile.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarScreens) {
                BottomNavigationBar(
                    navController = navController
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)


        ) {
            // Splash
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route){
                            popUpTo(Screen.Splash.route){
                                inclusive = true
                            }
                        }
                    },
                    viewModel = hiltViewModel()
                )
            }

            // Login
            composable(Screen.Login.route) {
                val viewModel: AuthViewModel = hiltViewModel()

                LoginScreen(
                    onOtpSent = {
                        navController.navigate(Screen.Otp.route)
                    },
                    viewModel = viewModel
                )
            }
            // OTP
            composable(Screen.Otp.route) {
                val viewModel: AuthViewModel = hiltViewModel(
                    navController.getBackStackEntry(Screen.Login.route)
                )

                OtpScreen(
                    viewModel = viewModel,
                    onExistingUser = {
                        navController.navigate(Screen.Home.route)
                    },
                    onNewUser = {
                        navController.navigate(Screen.CompleteProfile.route)
                    }
                )
            }
            // Home
            composable(Screen.Home.route) {
                HomeScreen()
            }

            // Upload
            composable(Screen.Upload.route) {
                UploadScreen()
            }

            // Profile
            composable(Screen.Profile.route) {
                val viewModel: ProfileViewModel = hiltViewModel()
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onEditProfile = {
                        navController.navigate(Screen.EditProfile.route)
                    },
                    onContacts = {
                        navController.navigate(Screen.Contacts.route)
                    },
                    viewModel = viewModel
                )
            }


            composable(Screen.EditProfile.route) {
                val viewModel: EditProfileViewModel = hiltViewModel()
                EditProfileScreen( onBack = {
                    navController.popBackStack()
                },
                    viewModel = viewModel
                    )
            }

            composable(Screen.Contacts.route) {
                ContactsScreen()
            }

            composable(Screen.CompleteProfile.route) {
                val viewModel: CompleteProfileViewModel = hiltViewModel()

                CompleteProfileScreen(
                    onBack = {
                        navController.popBackStack()
                    },
                    onProfileCreated = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.CompleteProfile.route) {
                                inclusive = true
                            }
                        }
                    },
                    viewModel = viewModel
                )
            }

        }
    }
}

