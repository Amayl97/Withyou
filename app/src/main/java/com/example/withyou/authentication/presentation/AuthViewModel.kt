package com.example.withyou.authentication.presentation

import androidx.lifecycle.ViewModel
import com.example.withyou.authentication.data.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthenticationRepository
): ViewModel(){}
