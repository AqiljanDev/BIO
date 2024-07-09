package com.example.data

import android.content.Context
import android.content.SharedPreferences

class SearchHistoryManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("search_history_prefs", Context.MODE_PRIVATE)

    fun saveQuery(query: String) {
        val history = getHistory().toMutableList()
        if (query in history) {
            history.remove(query)
        }
        history.add(0, query)
        sharedPreferences.edit().putStringSet("search_history", history.toSet()).apply()
    }

    fun getHistory(): List<String> {
        return sharedPreferences.getStringSet("search_history", emptySet())?.toList() ?: emptyList()
    }

    fun clearHistory() {
        sharedPreferences.edit().remove("search_history").apply()
    }
}



