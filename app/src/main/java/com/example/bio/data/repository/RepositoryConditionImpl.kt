package com.example.bio.data.repository

import com.example.bio.data.datasource.ConditionRemoteDataSourceImpl
import com.example.bio.data.dto.PostCartDto
import com.example.bio.domain.entities.cart.CartFull
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.compare.CompareFull
import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.bio.domain.repository.ConditionRemoteDataSource
import com.example.bio.domain.repository.RepositoryCondition
import javax.inject.Inject

class RepositoryConditionImpl @Inject constructor(
    private val conditionRemoteDataSource: ConditionRemoteDataSource
) : RepositoryCondition {
    override suspend fun getMiniWishList(token: String): List<WishListCompareMini> {
        return conditionRemoteDataSource.getMiniWishList(token)
    }

    override suspend fun postWishListEvent(token: String, id1c: String): List<WishListCompareMini> {
        return conditionRemoteDataSource.postWishListEvent(token, id1c)
    }


    override suspend fun getMiniCompare(token: String): List<WishListCompareMini> {
        return conditionRemoteDataSource.getMiniCompare(token)
    }

    override suspend fun postCompareEvent(token: String, id1c: String): List<WishListCompareMini> {
        return conditionRemoteDataSource.postCompareEvent(token, id1c)
    }

    override suspend fun getCompareFull(token: String): CompareFull {
        return conditionRemoteDataSource.getCompareFull(token)
    }


    override suspend fun getMiniCart(token: String): CartMini {
        return conditionRemoteDataSource.getMiniCart(token)
    }

    override suspend fun postCartEvent(token: String, postCart: PostCart): CartMini {
        return conditionRemoteDataSource.postCartEvent(token, postCart as PostCartDto)
    }

    override suspend fun removeCart(token: String, id: Int): CartMini {
        return conditionRemoteDataSource.removeCart(token, id)
    }

    override suspend fun getCartFull(token: String): CartFull {
        return conditionRemoteDataSource.getCartFull(token)
    }
}