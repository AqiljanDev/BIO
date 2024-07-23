package com.example.login.data.dataClass.returnData

import com.google.gson.annotations.SerializedName

data class ApiError (
    val message: Any? = null,
    val error: String? = null,
    val statusCode: Int? = null
)
