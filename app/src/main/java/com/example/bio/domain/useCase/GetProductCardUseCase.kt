package com.example.bio.domain.useCase

import com.example.bio.domain.entities.findOneProduct.FindOneProduct
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class GetProductCardUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun execute(token: String, category: String): FindOneProduct {
        return repository.getProductCard(token, category)
    }
}