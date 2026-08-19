package com.example.withyou.ui.screens.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.withyou.data.model.Contact
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(
    navController: NavController,
    viewModel: ContactsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val inviteMessage =
        "Join me on WithYou! Download the app and stay connected."

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
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopAppBar(
            title = {
                Text(
                    text = "Contacts"
                )
            },

            navigationIcon = {
                IconButton(
                    onClick = {
                        // Goes back to the previous screen.
                        // This is handled by the Navigation back stack.
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },

            actions = {
                IconButton(
                    onClick = {
                        // Reloads the latest contacts from the device.
                        viewModel.loadContacts()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh contacts"
                    )
                }
            }
        )
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { query ->
                viewModel.onSearchQueryChanged(query)
            },
            label = {
                Text("Search contacts")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

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
                if (uiState.searchQuery.isNotBlank()) {
                    NoSearchResultsContent()
                } else {
                    EmptyContactsContent()
                }
            }

            else -> {
                ContactsList(
                    contacts = uiState.contacts,
                    onInviteClick = {

                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"

                            putExtra(
                                Intent.EXTRA_TEXT,
                                inviteMessage
                            )
                        }

                        val chooserIntent = Intent.createChooser(
                            shareIntent,
                            "Invite via"
                        )

                        context.startActivity(chooserIntent)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ContactsList(
    contacts: List<Contact>,
    onInviteClick: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {

        items(
            items = contacts,
            key = { contact -> contact.id }
        ) { contact ->

            ContactItem(
                contact = contact,
                onInviteClick = onInviteClick
            )
        }
    }
}


@Composable
fun ErrorContent(
    message: String
) {
    ContactsStateContent(
        message = message
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Loading contacts..."
        )
    }
}
@Composable
fun NoSearchResultsContent() {
    ContactsStateContent(
        message = "No results found."
    )
}
@Composable
fun EmptyContactsContent() {
    ContactsStateContent(
        message = "No contacts found."
    )
}
@Composable
fun ContactsStateContent(
    message: String
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message)
    }
}