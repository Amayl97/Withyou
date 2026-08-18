package com.example.withyou.ui.screens.contacts

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//PHONE CONTACTS
//↑
//ContactsContract
//↑
//ContactsRepository
//↓ List<Contact>
//ContactsViewModel
//↓ ContactsUiState
//ContactsScreen
//↓
//USER SEES CONTACTS


// ViewModel responsible for managing the state of the Contacts screen.
// It requests contact data from ContactsRepository and exposes
// the resulting UI state to the ContactsScreen.

class ContactsViewModel(
    private val contactsRepository: ContactsRepository
): ViewModel(){
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
                        isLoading = false
                    )
                }

            } catch (e: Exception){
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load contacts"
                    )
                }
            }
        }
    }
}