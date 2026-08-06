package com.example.withyou.ui.screens.splash

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit
){
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigateToLogin()
    }
    Text("Splash Screen")
}