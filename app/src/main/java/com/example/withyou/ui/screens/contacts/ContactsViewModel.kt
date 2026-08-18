package com.example.withyou.ui.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())

    val uiState = _uiState.asStateFlow()

    fun loadContacts() {
        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }

            try {
                val contacts = withContext(Dispatchers.IO) {
                    contactsRepository.getContact()
                }

                _uiState.update {
                    it.copy(
                        contacts = contacts,
                        isLoading = false,
                        error = null
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load contacts"
                    )
                }
            }
        }
    }

    fun onPermissionDenied() {
        _uiState.update {
            it.copy(
                isPermissionDenied = true,
                isLoading = false
            )
        }
    }
}