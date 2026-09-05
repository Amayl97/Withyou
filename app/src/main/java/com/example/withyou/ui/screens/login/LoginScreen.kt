package com.example.withyou.ui.screens.login

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import com.example.withyou.ui.theme.AppSpacing
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.withyou.authentication.presentation.AuthViewModel
import com.example.withyou.ui.theme.Primary
import com.example.withyou.ui.theme.WhiteBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onOtpSent: () -> Unit,
    viewModel: AuthViewModel
) {
    var countryCode by remember {
        mutableStateOf("+92")
    }
    var countrySearch by remember {
        mutableStateOf("")
    }

    var countryDropdownExpanded by remember {
        mutableStateOf(false)
    }
    var phoneNumber by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val activity = context as Activity
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .padding(horizontal = AppSpacing.Medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(com.example.withyou.R.drawable.logo),
            contentDescription = "WithYou Logo",
            modifier = Modifier.size(170.dp)
        )

        Spacer(
            modifier = Modifier.height(AppSpacing.Small)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.Small)
        ) {

            ExposedDropdownMenuBox(
                expanded = countryDropdownExpanded,
                onExpandedChange = {
                    countryDropdownExpanded = !countryDropdownExpanded
                },
                modifier = Modifier.width(120.dp)
            ) {

                OutlinedTextField(
                    value = countrySearch.ifBlank { countryCode },
                    onValueChange = {
                        countrySearch = it
                        countryDropdownExpanded = true
                    },
                    label = {
                        Text(
                            text = "Code",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = countryDropdownExpanded
                        )
                    },
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                val filteredCountries = countryCodes.filter { country ->
                    country.countryName.contains(
                        countrySearch,
                        ignoreCase = true
                    ) ||
                            country.code.contains(
                                countrySearch,
                                ignoreCase = true
                            )
                }

                ExposedDropdownMenu(
                    expanded = countryDropdownExpanded,
                    onDismissRequest = {
                        countryDropdownExpanded = false
                    }
                ) {

                    filteredCountries.forEach { country ->

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${country.countryName} ${country.code}"
                                )
                            },
                            onClick = {
                                countryCode = country.code
                                countrySearch = ""
                                countryDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                },
                label = {
                    Text(
                        text = "Phone Number",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                )
            )
        }

        Spacer(
            modifier = Modifier.height(AppSpacing.Medium)
        )

        Button(
            onClick = {
                viewModel.sendOtp(
                    phoneNumber = countryCode + phoneNumber,
                    activity = activity,
                    onOtpSent = onOtpSent
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = WhiteBackground
            ),
            contentPadding = PaddingValues(
                horizontal = AppSpacing.FullSpace,
                vertical = AppSpacing.Medium
            ),
            shape = MaterialTheme.shapes.medium,
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = "Send OTP",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        if (errorMessage != null) {
            Spacer(
                modifier = Modifier.height(AppSpacing.Small)
            )

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}