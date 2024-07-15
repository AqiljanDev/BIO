package com.example.bio.domain.useCase

import com.example.bio.domain.entities.myOrder.MyOrder
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class GetOrdersFindMyUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun execute(token: String): List<MyOrder> {
        return repository.getOrdersFindMy(token)
    }
}