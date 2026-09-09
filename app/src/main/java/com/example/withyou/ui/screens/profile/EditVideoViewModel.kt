package com.example.withyou.ui.screens.profile

import androidx.compose.material3.Text
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withyou.data.model.Contact
import com.example.withyou.data.model.Permission
import com.example.withyou.data.repository.ContactsRepository
import com.example.withyou.data.repository.PermissionRepository
import com.example.withyou.data.repository.UserRepository
import com.example.withyou.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class EditVideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val contactsRepository: ContactsRepository,
    private val userRepository: UserRepository,
    private val permissionRepository: PermissionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditVideoUiState())
    val uiState: StateFlow<EditVideoUiState> = _uiState.asStateFlow()
    // We'll add the state and loading logic here next.
    fun loadVideo(videoId: String) {
        viewModelScope.launch {

            _uiState.value = EditVideoUiState(
                isLoading = true
            )

            val result = videoRepository.getVideoById(videoId)

            result
                .onSuccess { video ->
                    _uiState.value = EditVideoUiState(
                        video = video,
                        title = video.title,
                        description = video.description,
                        visibility = video.visibility,
                        selectedContactIds = video.allowedContactIds
                    )
                }
                .onFailure { error ->
                    _uiState.value = EditVideoUiState(
                        error = error.message
                    )
                }
        }
    }
    fun loadContactsForEditing() {
        viewModelScope.launch {

            val contacts = contactsRepository.getContact()

            val availableContacts = contacts.filter { contact ->
                userRepository.getUserByPhoneNumber(contact.phoneNumber) != null
            }

            val selectedContacts = availableContacts.filter { contact ->
                val user = userRepository.getUserByPhoneNumber(contact.phoneNumber)
                user?.uid in _uiState.value.selectedContactIds
            }

            _uiState.value = _uiState.value.copy(
                contacts = availableContacts,
                selectedContacts = selectedContacts
            )
        }
    }
    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title
        )
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(
            description = description
        )
    }
    fun updateVisibility(visibility: String) {
        _uiState.value = _uiState.value.copy(
            visibility = visibility
        )
    }
    fun onContactSelected(contact: Contact) {
        val currentSelected = _uiState.value.selectedContacts

        val isSelected = currentSelected.any {
            it.id == contact.id
        }

        val updatedSelected =
            if (isSelected) {
                currentSelected.filter {
                    it.id != contact.id
                }
            } else {
                currentSelected + contact
            }

        _uiState.value = _uiState.value.copy(
            selectedContacts = updatedSelected
        )

        viewModelScope.launch {

            val updatedSelectedContactIds =
                updatedSelected.mapNotNull { selectedContact ->
                    userRepository
                        .getUserByPhoneNumber(selectedContact.phoneNumber)
                        ?.uid
                }

            _uiState.value = _uiState.value.copy(
                selectedContactIds = updatedSelectedContactIds
            )
        }
    }
    fun saveChanges() {
        val currentState = _uiState.value
        val video = currentState.video ?: return

        _uiState.value = _uiState.value.copy(
            error = null,
            isSaved = false,
            isSaving = true
        )

        viewModelScope.launch {

            try {
                // 1. Get existing permissions
                val existingPermissions =
                    permissionRepository
                        .getPermissions(video.id)
                        .getOrThrow()

                val existingUserIds =
                    existingPermissions
                        .map { it.userId }
                        .toSet()

                // 2. Determine new allowed users
                val newUserIds =
                    if (currentState.visibility == "selected_contacts") {
                        currentState.selectedContactIds.toSet()
                    } else {
                        emptySet()
                    }

                // 3. Users who should be removed
                val usersToRemove =
                    existingUserIds - newUserIds

                // 4. Users who should be added
                val usersToAdd =
                    newUserIds - existingUserIds

                // 5. Delete removed permissions
                existingPermissions
                    .filter { it.userId in usersToRemove }
                    .forEach { permission ->

                        permissionRepository
                            .deletePermission(
                                videoId = video.id,
                                userId = permission.userId
                            )
                            .getOrThrow()
                    }

                // 6. Create new permissions
                usersToAdd.forEach { userId ->

                    val permission = Permission(
                        userId = userId,
                        videoId = video.id,
                        createdAt = System.currentTimeMillis()
                    )

                    permissionRepository
                        .createPermission(permission)
                        .getOrThrow()
                }

                // 7. Update video metadata
                val updatedVideo = video.copy(
                    title = currentState.title.trim(),
                    description = currentState.description.trim(),
                    visibility = currentState.visibility,
                    allowedContactIds = newUserIds.toList()
                )

                videoRepository
                    .saveVideo(updatedVideo)
                    .getOrThrow()

                // 8. Update UI state after successful save
                _uiState.value = _uiState.value.copy(
                    video = updatedVideo,
                    error = null,
                    isSaved = true,
                    isSaving = false
                )

            } catch (e: Exception) {

                // Save failed
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Failed to save changes",
                    isSaved = false,
                    isSaving = false
                )
            }
        }
    }
}
