package com.example.login.data.api

import com.example.core.UrlConstants.BASE_URL
import com.example.login.data.dataClass.LoginData
import com.example.login.data.dataClass.RegisterData
import com.example.login.data.dataClass.returnData.AuthReturnClass
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

val retrofitAuth = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(AuthenticationApi::class.java)

interface AuthenticationApi {

    @POST("auth/login")
    suspend fun login(@Body loginData: LoginData): AuthReturnClass

    @POST("auth/registration")
    suspend fun registration(@Body registerData: RegisterData): AuthReturnClass

    @GET("auth/check")
    suspend fun testGet(
        @Header("Authorization") token: String
    )
}