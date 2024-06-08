package com.example.login.ui.main

sealed class StateSealedClass {
    class Success(val token: String) : StateSealedClass()
    data object Loading : StateSealedClass()
    class Failed(val message: String) : StateSealedClass()
}