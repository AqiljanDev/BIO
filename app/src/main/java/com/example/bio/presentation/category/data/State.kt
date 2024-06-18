package com.example.bio.presentation.category.data
sealed class State {
    data object Loading : State()
    data object Success : State()
    class Failed(val message: String) : State()
}