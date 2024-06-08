package com.example.login.data.dataClass

import com.google.gson.annotations.SerializedName


data class LoginData(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)
