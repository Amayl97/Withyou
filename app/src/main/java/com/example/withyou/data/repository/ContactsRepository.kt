package com.example.withyou.data.repository

import android.content.Context
import android.provider.ContactsContract
import com.example.withyou.data.model.Contact

//    In this class we will read contacts from android
// Repository responsible for reading contact data
// from the Android device using ContactsContract.
// It converts Android contact data into our app's Contact model.
class ContactsRepository(
    private val context: Context
){

fun getContact(): List<Contact>{
    val contacts = mutableListOf<Contact>()
    val cursor= context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        ),
        null,
        null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )
    cursor?.use { cursor ->

        while (cursor.moveToNext()) {

            val id = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                )
            )

            val name = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                )
            )

            val phoneNumber = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                )
            )

            contacts.add(
                Contact(
                    id = id,
                    name = name,
                    phoneNumber = phoneNumber
                )
            )
        }
    }
    return contacts
  }
}