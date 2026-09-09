package com.example.withyou.ui.screens.profile


import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.withyou.ui.theme.AppSpacing

@Composable
fun EditVideoScreen(
    videoId: String,
    onBack: () -> Unit,
    viewModel: EditVideoViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(videoId) {
        viewModel.loadVideo(videoId)
        viewModel.loadContactsForEditing()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.Medium)
    ) {

        // Back button
        IconButton(
            onClick = onBack
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        Spacer(
            modifier = Modifier.height(AppSpacing.Medium)
        )

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            uiState.video != null -> {

                Text(
                    text = "Edit Video",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Medium)
                )

                // Title
                Text(
                    text = "Title:",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Small)
                )

                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = {
                        viewModel.updateTitle(it)
                    },
                    label = {
                        Text("Title")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Medium)
                )

                // Description
                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Small)
                )

                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = {
                        viewModel.updateDescription(it)
                    },
                    label = {
                        Text("Description")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = MaterialTheme.shapes.medium
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Medium)
                )

                // Privacy
                Text(
                    text = "Privacy:",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(AppSpacing.Small)
                )

                var privacyExpanded by remember {
                    mutableStateOf(false)
                }

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = {
                            privacyExpanded = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = when (uiState.visibility) {
                                "private" -> "Private"
                                "contacts" -> "My contacts"
                                "selected_contacts" -> "Selected contacts"
                                else -> "Private"
                            }
                        )
                    }


                    DropdownMenu(
                        expanded = privacyExpanded,
                        onDismissRequest = {
                            privacyExpanded = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text("Private")
                            },
                            onClick = {
                                viewModel.updateVisibility("private")
                                privacyExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("My contacts")
                            },
                            onClick = {
                                viewModel.updateVisibility("contacts")
                                privacyExpanded = false
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text("Selected contacts")
                            },
                            onClick = {
                                viewModel.updateVisibility("selected_contacts")
                                privacyExpanded = false
                            }
                        )
                    }
                }
                    //closes the box
                    // Selected contacts
                    if (uiState.visibility == "selected_contacts") {

                        Spacer(
                            modifier = Modifier.height(AppSpacing.Medium)
                        )

                        Text(
                            text = "Select contacts:",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(
                            modifier = Modifier.height(AppSpacing.Small)
                        )

                        if (uiState.contacts.isEmpty()) {

                            Text(
                                text = "No WithYou contacts found",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                        } else {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.medium)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = MaterialTheme.shapes.medium
                                    )
                            ) {

                                uiState.contacts.forEach { contact ->

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.onContactSelected(contact)
                                            }
                                            .padding(
                                                horizontal = AppSpacing.Small,
                                                vertical = AppSpacing.Small
                                            ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Checkbox(
                                            checked = uiState.selectedContacts.any {
                                                it.id == contact.id
                                            },
                                            onCheckedChange = {
                                                viewModel.onContactSelected(contact)
                                            }
                                        )

                                        Column(
                                            modifier = Modifier.padding(
                                                start = AppSpacing.Small
                                            )
                                        ) {

                                            Text(
                                                text = contact.name
                                            )

                                            Text(
                                                text = contact.phoneNumber,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                Spacer(
                    modifier = Modifier.height(AppSpacing.Large)
                )

                Button(
                    onClick = {
                        viewModel.saveChanges()
                    },
                    enabled = !uiState.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = when {
                            uiState.isSaving -> "Saving..."
                            uiState.isSaved -> "Saved"
                            uiState.error != null -> "Retry"
                            else -> "Save"
                        }
                    )
                }
               }
            }
        }
    }
