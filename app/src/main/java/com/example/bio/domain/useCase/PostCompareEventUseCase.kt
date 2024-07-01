package com.example.bio.domain.useCase

import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.bio.domain.repository.RepositoryCondition
import javax.inject.Inject

class PostCompareEventUseCase @Inject constructor(
    private val repositoryCondition: RepositoryCondition
) {

    suspend fun execute(token: String, id1c: String): List<WishListCompareMini> {
        return repositoryCondition.postCompareEvent(token, id1c)
    }
}