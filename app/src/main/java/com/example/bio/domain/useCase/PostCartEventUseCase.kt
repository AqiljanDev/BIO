package com.example.bio.domain.useCase

import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.bio.domain.repository.RepositoryCondition
import javax.inject.Inject

class PostCartEventUseCase @Inject constructor(
    private val repositoryCondition: RepositoryCondition
) {

    suspend fun execute(token: String, postCart: PostCart): CartMini {
        return repositoryCondition.postCartEvent(token, postCart)
    }
}