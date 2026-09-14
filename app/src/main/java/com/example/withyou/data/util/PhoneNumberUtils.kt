package com.example.withyou.data.util


object PhoneNumberUtils {

    fun normalize(
        countryCode: String,
        phoneNumber: String
    ): String {

        val code = countryCode
            .replace(Regex("[^0-9]"), "")

        val number = phoneNumber
            .replace(Regex("[^0-9]"), "")
            .removePrefix("0")

        return "+$code$number"
    }

    fun normalize(phoneNumber: String): String {

        val digits = phoneNumber
            .replace(Regex("[^0-9]"), "")

        return if (digits.startsWith("0")) {
            "+92${digits.removePrefix("0")}"
        } else {
            "+$digits"
        }
    }
}