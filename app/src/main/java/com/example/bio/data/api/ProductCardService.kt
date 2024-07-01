package com.example.bio.data.api

import com.example.bio.data.dto.FindOneProductDto
import com.example.core.UrlConstants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

val retrofitProductCard by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProductCardService::class.java)
}

interface ProductCardService {

    @GET("products/{category}")
    suspend fun findOneProduct(
        @Header("Authorization") token: String,
        @Path("category") category: String
    ): FindOneProductDto
}