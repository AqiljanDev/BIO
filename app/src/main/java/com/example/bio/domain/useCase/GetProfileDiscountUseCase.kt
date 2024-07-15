package com.example.bio.domain.useCase

import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class GetProfileDiscountUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun execute(token: String): List<UserDiscount> {
        return repository.getProfileDiscount(token)
    }
}