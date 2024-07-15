package com.example.bio.domain.useCase

import com.example.bio.domain.entities.findOneOrder.FindOneOrder
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class GetOrdersFindOneUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun execute(token: String, id: Int): FindOneOrder {
        return repository.getOrdersFindOne(token, id)
    }
}