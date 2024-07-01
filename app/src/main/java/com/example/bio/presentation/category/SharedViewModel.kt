package com.example.bio.presentation.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _currentCategory = MutableLiveData<String>()
    val currentCategory: LiveData<String>
        get() = _currentCategory

    fun setCurrentCategory(category: String) {
        _currentCategory.value = category
    }
}

