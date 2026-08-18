package com.example.withyou.ui.screens.contacts

import com.example.withyou.data.model.Contact

// Represents the current UI state of the Contacts screen,
// including the contact list, loading state, and possible errors.
data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val isLoading: Boolean = false,
    val isPermissionDenied: Boolean = false,
    val error: String? = null
)