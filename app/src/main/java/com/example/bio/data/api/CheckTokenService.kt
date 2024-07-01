package com.example.bio.data.api

import com.example.core.UrlConstants.BASE_URL
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

val retrofitCheckToken: CheckTokenService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CheckTokenService::class.java)
}

interface CheckTokenService {

    @GET("auth/check")
    suspend fun testGet(
        @Header("Authorization") token: String
    )
}
