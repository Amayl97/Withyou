package com.example.withyou.ui.screens.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.model.Contact
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
    // Stores the complete contact list from the device.
    // This list is used as the source when filtering search results.
    private var allContacts = emptyList<Contact>()

    fun loadContacts() {
        viewModelScope.launch {

            // Show loading state while contacts are being read.
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {
                // Reads the latest contacts from the device.
                val contacts = withContext(Dispatchers.IO) {
                    contactsRepository.getContact()
                }

                // Stores the complete list separately.
                // This list is used as the source for searching.
                allContacts = contacts

                // Updates the contacts displayed on the screen.
                _uiState.update {
                    it.copy(
                        contacts = filterContacts(
                            contacts = allContacts,
                            query = it.searchQuery
                        ),
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

    fun onSearchQueryChanged(query: String) {

        _uiState.update {
            it.copy(
                searchQuery = query,
                contacts = filterContacts(
                    contacts = allContacts,
                    query = query
                )
            )
        }
    }
    private fun filterContacts(
        contacts: List<Contact>,
        query: String
    ): List<Contact> {

        if (query.isBlank()) {
            return contacts
        }

        return contacts.filter { contact ->

            contact.name.contains(
                other = query,
                ignoreCase = true
            ) ||
                    contact.phoneNumber.contains(
                        other = query,
                        ignoreCase = true
                    )
        }
    }
}