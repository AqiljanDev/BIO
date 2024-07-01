package com.example.bio.domain.repository

import com.example.bio.data.dto.CartFullDto
import com.example.bio.data.dto.PostCartDto
import com.example.bio.domain.entities.cart.CartFull
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.compare.CompareFull
import com.example.bio.domain.entities.wishList.WishListCompareMini

interface ConditionRemoteDataSource {

    suspend fun getMiniWishList(token: String): List<WishListCompareMini>
    suspend fun postWishListEvent(token: String, id1c: String): List<WishListCompareMini>

    suspend fun getMiniCompare(token: String): List<WishListCompareMini>
    suspend fun postCompareEvent(token: String, id1c: String): List<WishListCompareMini>
    suspend fun getCompareFull(token: String): CompareFull

    suspend fun getMiniCart(token: String): CartMini
    suspend fun postCartEvent(token: String, postCart: PostCartDto): CartMini
    suspend fun removeCart(token: String, id: Int): CartMini
    suspend fun getCartFull(token: String): CartFull
}
