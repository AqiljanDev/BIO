package com.example.bio.data.api

import com.example.bio.data.dto.CartFullDto
import com.example.bio.data.dto.CartMiniDto
import com.example.bio.data.dto.CreateCheckout
import com.example.bio.data.dto.OrderDetails
import com.example.bio.data.dto.PostCartDto
import com.example.bio.data.dto.WishListCompareMiniDto
import com.example.bio.data.dto.WishListFullDto
import com.example.bio.data.dto.compare.CompareFullDto
import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.core.UrlConstants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


val retrofitProductCondition: ProductConditionService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProductConditionService::class.java)
}

interface ProductConditionService {

    @POST("wishlist/{id}")
    suspend fun postWishListEvent(
        @Header("Authorization") token: String,
        @Path("id") id1c: String
    ): List<WishListCompareMiniDto>

    @GET("wishlist/mini")
    suspend fun getMiniWishList(
        @Header("Authorization") token: String
    ): List<WishListCompareMiniDto>

    @GET("wishlist")
    suspend fun getFullWishList(
        @Header("Authorization") token: String
    ): List<WishListFullDto>


    @POST("compare/{id}")
    suspend fun postCompareEvent(
        @Header("Authorization") token: String,
        @Path("id") id1c: String
    ): List<WishListCompareMiniDto>

    @GET("compare/mini")
    suspend fun getMiniCompare(
        @Header("Authorization") token: String
    ): List<WishListCompareMiniDto>

    @GET("compare/")
    suspend fun getCompareFull(
        @Header("Authorization") token: String
    ): CompareFullDto

    @POST("cart/")
    suspend fun postCartEvent(
        @Header("Authorization") token: String,
        @Body postCart: PostCartDto
    ): CartMiniDto

    @GET("cart/mini")
    suspend fun getCartMini(
        @Header("Authorization") token: String,
    ): CartMiniDto

    @DELETE("cart/{id}")
    suspend fun removeProdCart(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): CartMiniDto

    @GET("cart")
    suspend fun getCartFull(
        @Header("Authorization") token: String
    ): CartFullDto

    @POST("orders")
    suspend fun createCheckout(
        @Header("Authorization") token: String,
        @Body data: CreateCheckout
    ): OrderDetails
}
