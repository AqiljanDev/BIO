package com.example.bio.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

private const val BASE_URL: String = "http://192.168.8.3:4040/api/auth/"

val retrofitCheckToken: CheckTokenService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CheckTokenService::class.java)
}

interface CheckTokenService {

    @GET("check")
    suspend fun testGet(
        @Header("Authorization") token: String
    )
}
