package com.example.login.data.api

import com.example.core.UrlConstants.BASE_URL
import com.example.login.data.dataClass.PasswordChangeData
import com.example.login.data.dataClass.PasswordCodeCheckData
import com.example.login.data.dataClass.PasswordCodeSendData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

val retrofitPassRestore: PasswordRestore = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(PasswordRestore::class.java)

interface PasswordRestore {

    @POST("auth/password/code/send")
    suspend fun passwordCodeSend(@Body passwordCodeSendData: PasswordCodeSendData)

    @POST("auth/password/code/check")
    suspend fun passwordCodeCheck(@Body passwordCodeCheckData: PasswordCodeCheckData): Boolean

    @POST("auth/password/change")
    suspend fun passwordChange(@Body passwordChangeData: PasswordChangeData): Boolean
}