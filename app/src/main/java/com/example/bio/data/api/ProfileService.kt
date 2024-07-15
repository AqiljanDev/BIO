package com.example.bio.data.api

import com.example.bio.data.dto.findOneOrder.FindOneOrderDto
import com.example.bio.data.dto.myOrder.MyOrderDto
import com.example.bio.data.dto.user.UserDiscountDto
import com.example.core.UrlConstants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path


val retrofitProfile: ProfileService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProfileService::class.java)
}

interface ProfileService {

    @GET("discount")
    suspend fun getDiscountUser(
        @Header("Authorization") token: String
    ): List<UserDiscountDto>

    @GET("orders/my")
    suspend fun getFindMy(
        @Header("Authorization") token: String
    ): List<MyOrderDto>

    @GET("orders/{id}")
    suspend fun getFindOne(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): FindOneOrderDto
}