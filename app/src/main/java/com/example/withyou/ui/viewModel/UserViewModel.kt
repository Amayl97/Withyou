package com.example.withyou.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.model.User
import com.example.withyou.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun createUser(user: User) {
        viewModelScope.launch {
            userRepository.createUser(user)
        }
    }

    fun getUser(uid: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.getUser(uid)
            onResult(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

}