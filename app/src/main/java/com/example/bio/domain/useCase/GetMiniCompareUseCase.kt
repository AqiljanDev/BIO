package com.example.bio.domain.useCase

import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.bio.domain.repository.RepositoryCondition
import javax.inject.Inject

class GetMiniCompareUseCase @Inject constructor(
    private val repository: RepositoryCondition
) {

    suspend fun execute(token: String): List<WishListCompareMini> {
        return repository.getMiniCompare(token)
    }
}
