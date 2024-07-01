package com.example.bio.data.api

import com.example.bio.data.dto.CatalogDto
import com.example.bio.data.dto.CategoriesFindAllDto
import com.example.bio.data.dto.ChildCategoryDto
import com.example.bio.data.dto.collectCharacters.CollectCharactersDto
import com.example.bio.domain.entities.CategoriesFindAll
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.entities.findOne.ChildCategory
import com.example.core.UrlConstants.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

val retrofitCatalog: CatalogService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CatalogService::class.java)
}

interface CatalogService {

    @GET("catalog/{category}")
    suspend fun findOne(
        @Header("Authorization") token: String,
        @Path("category") category: String,
        @Query("page") page: Int
    ): CatalogDto

    @GET("catalog/char/{category}")
    suspend fun collectCharacters(
        @Header("Authorization") token: String,
        @Path("category") category: String
    ): CollectCharactersDto

    @GET("categories/")
    suspend fun categoriesFindAll(
        @Header("Authorization") token: String
    ): List<CategoriesFindAllDto>
}