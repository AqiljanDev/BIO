package com.example.bio.domain.useCase

import com.example.bio.domain.entities.cart.CartFull
import com.example.bio.domain.repository.RepositoryCondition
import javax.inject.Inject

class GetCartFullUseCase @Inject constructor(
    private val repositoryCondition: RepositoryCondition
) {

    suspend fun execute(token: String): CartFull {
        return repositoryCondition.getCartFull(token)
    }
}
