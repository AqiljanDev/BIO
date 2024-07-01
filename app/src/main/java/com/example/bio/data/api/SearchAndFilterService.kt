package com.example.bio.data.api

import com.example.bio.data.dto.CatalogDto
import com.example.bio.data.dto.ProductDto
import com.example.bio.data.dto.collectCharacters.CollectCharactersDto
import com.example.core.UrlConstants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


val retrofitSearchAndFilter: SearchAndFilterService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SearchAndFilterService::class.java)
}

interface SearchAndFilterService {

    @GET("search")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("s") message: String
    ): List<ProductDto>

    @GET("catalog/")
    suspend fun findOne(
        @Header("Authorization") token: String,
        @Query("min") min: Int? = null,
        @Query("max") max: Int? = null,
        @Query("sort") sort: String,
        @Query("f") chars: String,
        @Query("page") page: Int
    ): CatalogDto

    @GET("char/{category}")
    suspend fun collectCharacters(
        @Header("Authorization") token: String,
        @Path("category") category: String,
        @Query("f") chars: String
    ): CollectCharactersDto

    @GET("catalog/brand/char/{category}")
    suspend fun collectCharactersBrand(
        @Header("Authorization") token: String,
        @Path("category") category: String
    ): CollectCharactersDto

}
