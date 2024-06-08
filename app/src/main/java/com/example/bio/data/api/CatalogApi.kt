package com.example.bio.data.api

import com.example.bio.data.dto.CatalogDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "http://localhost:4040/api/catalog"

val retrofitCatalog = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(CatalogApi::class.java)

interface CatalogApi {

    @GET()
    fun findOne() : CatalogDto
}