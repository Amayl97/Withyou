package com.example.withyou.ui.screens.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.withyou.data.model.Contact
import com.example.withyou.ui.theme.WhiteBackground

@Composable
fun ContactItem(
    contact: Contact,
    onInviteClick: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Displays the first letter of the contact's name.
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(WhiteBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = contact.name
                    .trim()
                    .firstOrNull()
                    ?.uppercase()
                    ?: "?"
            )
        }

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        // Displays contact information.
        Column(
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = contact.name
            )

            Spacer(
                modifier = Modifier.size(4.dp)
            )

            Text(
                text = contact.phoneNumber
            )
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {
                onInviteClick(contact)
            }
        ) {
            Text(
                text = "Invite"
            )
        }
    }
}