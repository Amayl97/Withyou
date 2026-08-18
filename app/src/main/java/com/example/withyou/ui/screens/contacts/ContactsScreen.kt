package com.example.withyou.ui.screens.contacts

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withyou.data.model.Contact

@Composable
fun ContactsScreen(
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Collects the current state from ContactsViewModel.
    // Whenever the state changes, this screen recomposes.
    val uiState by viewModel.uiState.collectAsState()

    // Handles the Android runtime permission request.
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->

        if (isGranted) {
            viewModel.loadContacts()
        } else {
            viewModel.onPermissionDenied()
        }
    }

    // Runs when the ContactsScreen is first opened.
    // If permission already exists, contacts are loaded.
    // Otherwise, the Android permission dialog is shown.
    LaunchedEffect(Unit) {

        val permissionGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {
            viewModel.loadContacts()
        } else {
            permissionLauncher.launch(
                Manifest.permission.READ_CONTACTS
            )
        }
    }

    // Displays different UI depending on the current screen state.
    when {

        uiState.isLoading -> {
            LoadingContent()
        }

        uiState.isPermissionDenied -> {
            PermissionDeniedContent()
        }

        uiState.error != null -> {
            ErrorContent(
                message = uiState.error ?: "Something went wrong"
            )
        }

        uiState.contacts.isEmpty() -> {
            EmptyContactsContent()
        }

        else -> {
            ContactsList(
                contacts = uiState.contacts
            )
        }
    }
}

@Composable
fun ContactsList(
    contacts: List<Contact>
) {
    LazyColumn {

        items(contacts) { contact ->

            Text(
                text = contact.name
            )
        }
    }
}

@Composable
fun EmptyContactsContent() {
    Text(
        text = "No contacts found."
    )
}

@Composable
fun ErrorContent(
    message: String
) {
    Text(
        text = message
    )
}

@Composable
fun PermissionDeniedContent() {
    Text(
        text = "Contact permission is required to display your contacts."
    )
}

@Composable
fun LoadingContent() {
    Text(
        text = "Loading contacts..."
    )
}