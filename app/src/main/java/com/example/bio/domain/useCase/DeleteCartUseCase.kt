package com.example.bio.domain.useCase

import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.repository.RepositoryCondition
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    private val repositoryCondition: RepositoryCondition
) {

    suspend fun execute(token: String, id: Int): CartMini {
        return repositoryCondition.removeCart(token, id)
    }
}
