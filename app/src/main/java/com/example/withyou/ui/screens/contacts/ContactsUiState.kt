package com.example.withyou.ui.screens.contacts

import com.example.withyou.data.model.Contact

data class ContactsUiState(

    // The contacts currently displayed on the screen.
    val contacts: List<Contact> = emptyList(),

    // Stores what the user types in the search bar.
    val searchQuery: String = "",

    val isLoading: Boolean = false,
    val isPermissionDenied: Boolean = false,
    val error: String? = null
)