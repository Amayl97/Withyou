package com.example.withyou.data.util


object PhoneNumberUtils {

    fun normalize(phoneNumber: String): String {

        val digits = phoneNumber
            .replace(Regex("[^0-9]"), "")

        return when {

            // Already starts with Pakistan country code
            digits.startsWith("92") -> {
                "+$digits"
            }

            // Local Pakistani format: 03XXXXXXXXX
            digits.startsWith("0") -> {
                "+92${digits.removePrefix("0")}"
            }

            else -> {
                "+$digits"
            }
        }
    }
}