package com.example.bio.data.dto

import com.google.gson.annotations.SerializedName

data class ReturnData (
        @SerializedName("access_token") val accessToken: String? = null,
        val message: String? = null,
        val error: String? = null,
        @SerializedName("statusCode") val statusCode: Int? = null
    )
