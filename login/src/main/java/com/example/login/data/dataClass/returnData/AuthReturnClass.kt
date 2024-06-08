package com.example.login.data.dataClass.returnData

import com.google.gson.annotations.SerializedName

data class AuthReturnClass(
    @SerializedName("access_token") val accessToken: String? = null,
    val message: String? = null,
    val error: String? = null,
    @SerializedName("statusCode") val statusCode: Int? = null
)
