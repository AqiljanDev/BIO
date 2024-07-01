package com.example.bio.domain.useCase

import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.repository.RepositoryCondition
import javax.inject.Inject

class GetMiniCartUseCase @Inject constructor(
    private val repository: RepositoryCondition
) {

    suspend fun execute(token: String): CartMini {
        return repository.getMiniCart(token)
    }
}