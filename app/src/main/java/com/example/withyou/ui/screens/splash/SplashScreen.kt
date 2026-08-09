package com.example.withyou.ui.screens.splash

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.withyou.authentication.presentation.AuthViewModel
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: AuthViewModel
){
    LaunchedEffect(Unit) {
        delay(2000)
        if(viewModel.isUserLoggedIn()){
            onNavigateToHome()
        }
        else{
            onNavigateToLogin()
        }
    }
    Text("Splash Screen")
}