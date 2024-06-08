package com.example.login.data.dataClass

import com.google.gson.annotations.SerializedName

data class RegisterData(
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("company") val company: String,
    @SerializedName("type") val type: String,
    @SerializedName("area") val area: String,
    @SerializedName("site") val site: String,
    @SerializedName("bin") val bin: String,
    @SerializedName("address") val address: String,
    @SerializedName("bik") val bik: String,
    @SerializedName("bank") val bank: String,
    @SerializedName("iik") val iik: String,
)
