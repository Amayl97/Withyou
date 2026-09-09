package com.example.withyou.ui.screens.profile

import com.example.withyou.data.model.Contact
import com.example.withyou.data.model.Video

data class EditVideoUiState(
    val video: Video? = null,
    val title: String = "",
    val description: String = "",
    val visibility: String = "private",
    val selectedContactIds: List<String> = emptyList(),
    val contacts: List<Contact> = emptyList(),
    val selectedContacts: List<Contact> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)