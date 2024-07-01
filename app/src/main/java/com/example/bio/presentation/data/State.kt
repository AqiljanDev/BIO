package com.example.bio.presentation.data
sealed class State {
    data object Loading : State()
    data object Success : State()
    class Failed(val message: String) : State()
}