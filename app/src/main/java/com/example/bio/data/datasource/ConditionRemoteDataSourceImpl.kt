package com.example.bio.data.datasource

import com.example.bio.data.api.retrofitProductCard
import com.example.bio.data.api.retrofitProductCondition
import com.example.bio.data.dto.CartFullDto
import com.example.bio.data.dto.PostCartDto
import com.example.bio.domain.entities.cart.CartFull
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.compare.CompareFull
import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.bio.domain.repository.ConditionRemoteDataSource
import javax.inject.Inject

class ConditionRemoteDataSourceImpl @Inject constructor() : ConditionRemoteDataSource {
    override suspend fun getMiniWishList(token: String): List<WishListCompareMini> {
        return retrofitProductCondition.getMiniWishList(token)
    }

    override suspend fun postWishListEvent(token: String, id1c: String): List<WishListCompareMini> {
        return retrofitProductCondition.postWishListEvent(token, id1c)
    }


    override suspend fun getMiniCompare(token: String): List<WishListCompareMini> {
        return retrofitProductCondition.getMiniCompare(token)
    }

    override suspend fun postCompareEvent(token: String, id1c: String): List<WishListCompareMini> {
        return retrofitProductCondition.postCompareEvent(token, id1c)
    }

    override suspend fun getCompareFull(token: String): CompareFull {
        return retrofitProductCondition.getCompareFull(token)
    }


    override suspend fun getMiniCart(token: String): CartMini {
        return retrofitProductCondition.getCartMini(token)
    }

    override suspend fun postCartEvent(token: String, postCart: PostCartDto): CartMini {
        return retrofitProductCondition.postCartEvent(token, postCart)
    }

    override suspend fun removeCart(token: String, id: Int): CartMini {
        return retrofitProductCondition.removeProdCart(token, id.toString())
    }

    override suspend fun getCartFull(token: String): CartFullDto {
        return retrofitProductCondition.getCartFull(token)
    }


}