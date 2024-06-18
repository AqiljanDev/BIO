package com.example.bio.data.api

import com.example.bio.data.dto.CatalogDto
import com.example.bio.data.dto.collectCharacters.CollectCharactersDto
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "http://192.168.8.3:4040/api/catalog/"

val retrofitCatalog: CatalogService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatalogService::class.java)
}

interface CatalogService {

    @GET("{category}")
    suspend fun findOne(
        @Header("Authorization") token: String,
        @Path("category") category: String,
        @Query("page") page: Int
    ): CatalogDto

    @GET("char/{category}")
    suspend fun collectCharacters(
        @Header("Authorization") token: String,
        @Path("category") category: String
    ): CollectCharactersDto

}