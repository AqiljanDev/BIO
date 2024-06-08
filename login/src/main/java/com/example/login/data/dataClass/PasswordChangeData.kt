package com.example.login.data.dataClass

data class PasswordChangeData(
    val code: String,
    val email: String,
    val passwordNew: String,
    val passwordVerify: String
)
