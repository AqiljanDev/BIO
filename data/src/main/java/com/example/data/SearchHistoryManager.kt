package com.example.data

import android.content.Context
import android.content.SharedPreferences

class SearchHistoryManager(context: Context) {
    private val preferences = context.getSharedPreferences("search_history_prefs", Context.MODE_PRIVATE)
    private val KEY_SEARCH_HISTORY = "search_history"

    fun getSearchHistory(): List<String> {
        return preferences.getStringSet(KEY_SEARCH_HISTORY, emptySet())?.toList() ?: emptyList()
    }

    fun saveSearchQuery(query: String) {
        val queries = getSearchHistory().toMutableList()
        if (!queries.contains(query)) {
            queries.add(query)
            saveSearchQueries(queries)
        }
    }

    fun clearHistory() {
        preferences.edit().remove(KEY_SEARCH_HISTORY).apply()
    }

    private fun saveSearchQueries(queries: List<String>) {
        val editor = preferences.edit()
        editor.putStringSet(KEY_SEARCH_HISTORY, queries.toSet())
        editor.apply()
    }
}

