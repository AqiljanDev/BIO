package com.example.login.data.api

import com.example.login.data.dataClass.PasswordChangeData
import com.example.login.data.dataClass.PasswordCodeCheckData
import com.example.login.data.dataClass.PasswordCodeSendData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL = "http://192.168.8.3/api/auth/password/"


val retrofitPassRestore: PasswordRestore = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(PasswordRestore::class.java)

interface PasswordRestore {

    @POST("code/send")
    suspend fun passwordCodeSend(@Body passwordCodeSendData: PasswordCodeSendData): Boolean

    @POST("code/check")
    suspend fun passwordCodeCheck(@Body passwordCodeCheckData: PasswordCodeCheckData): Boolean

    @POST("change")
    suspend fun passwordChange(@Body passwordChangeData: PasswordChangeData): Boolean
}