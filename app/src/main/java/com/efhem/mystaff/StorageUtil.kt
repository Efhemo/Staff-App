package com.efhem.mystaff

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Utility class for management of Shared Preferences storage.
 */
class StorageUtil(context: Context) {

    private var preferences: SharedPreferences =
        context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)

    /**
     * Current user email
     */
    var email: String
        get() = preferences.getString(EMAIL, "") ?: ""
        //Default behaviour is apply
        set(value) = preferences.edit { putString(EMAIL, value) }


    /**
     * Current user name
     */
    var name: String
        get() = preferences.getString(NAME, "") ?: ""
        //Default behaviour is apply
        set(value) = preferences.edit { putString(NAME, value) }



    companion object {
        private const val STORAGE = "com.efhem.mystaff.STORAGE"

        private const val EMAIL = "email"

        private const val NAME = "name"

    }
}