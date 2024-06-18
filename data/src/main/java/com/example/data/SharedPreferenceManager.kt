package com.example.data

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "shared_prefs"
        @Volatile private var instance: SharedPreferencesManager? = null

        fun getInstance(context: Context): SharedPreferencesManager =
            instance ?: synchronized(this) {
                instance ?: SharedPreferencesManager(context).also { instance = it }
            }
    }

    // Example methods to put/get data
    fun putString(key: KEYS, value: String) {
        sharedPreferences.edit().putString(key.message, value).apply()
    }

    fun getString(key: KEYS): String {
        return "Bearer ${sharedPreferences.getString(key.message, "DEFAULT_TOKEN") ?: "DEFAULT_TOKEN"}"
    }

    enum class KEYS(val message: String) {
        TOKEN("token_token")
    }

    // Add more methods as needed (e.g., putInt, getInt, etc.)
}
