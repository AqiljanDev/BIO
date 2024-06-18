package com.example.login.data.api

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

private const val BASE_URL = "http://192.168.8.3:4040/api/auth/"

val retrofitAuth = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(AuthenticationApi::class.java)

interface AuthenticationApi {

    @POST("login")
    suspend fun login(@Body loginData: LoginData): AuthReturnClass

    @POST("registration")
    suspend fun registration(@Body registerData: RegisterData): AuthReturnClass

    @GET("check")
    suspend fun testGet(
        @Header("Authorization") token: String
    )
}