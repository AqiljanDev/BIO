package com.example.bio.data.api

import com.example.bio.data.dto.ProductDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

private const val BASE_URL = "http://192.168.8.3:4040/api/"

val retrofitSearchAndFilter: SearchAndFilterService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchAndFilterService::class.java)
}


interface SearchAndFilterService {

    @GET("search?s={message}")
    fun search(
        @Header("Authorization") token: String,
        @Path("message") message: String
    ): List<ProductDto>

}