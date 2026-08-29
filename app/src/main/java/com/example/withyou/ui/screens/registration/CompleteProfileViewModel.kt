package com.example.withyou.ui.screens.registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.authentication.data.AuthenticationRepository
import com.example.withyou.data.model.User
import com.example.withyou.data.repository.UserRepository
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.example.withyou.data.util.PhoneNumberUtils
import dagger.hilt.android.lifecycle.HiltViewModel


@HiltViewModel
class CompleteProfileViewModel @Inject constructor(
  private val userRepository : UserRepository,
  private val authenticationRepository: AuthenticationRepository,


): ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage
    private fun getCurrentUserId(): String?{
        return authenticationRepository.getCurrentUserId()
    }

    fun createProfile(
        username: String,
        displayname: String,
        bio: String,
        onSuccess: () -> Unit
    ){
       val uid = getCurrentUserId()
        val phoneNumber =
            authenticationRepository.getCurrentUserPhoneNumber()
        if (uid == null){
            _errorMessage.value = "User not authenticated"
            return
        }
        if (phoneNumber == null) {
            _errorMessage.value = "Phone number not available"
            return
        }
        val user = User(
            uid = uid,
            username = username,
            displayName = displayname,
            phoneNumber = PhoneNumberUtils.normalize(phoneNumber),
            bio = bio
        )
        viewModelScope.launch {
           _isLoading.value = true
            _errorMessage.value = null
            try{
                userRepository.createUser(user)
                onSuccess()
            }catch (e: Exception){
                _errorMessage.value = e.message?: "Failed to create profile"

            }finally {
                _isLoading.value = false
            }
        }
    }


}
