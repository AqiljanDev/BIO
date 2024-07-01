package com.example.bio.domain.useCase

import com.example.bio.domain.entities.compare.CompareFull
import com.example.bio.domain.repository.RepositoryCondition
import javax.inject.Inject

class GetCompareFullUseCase @Inject constructor(
    private val repositoryCondition: RepositoryCondition
) {

    suspend fun execute(token: String): CompareFull {
        return repositoryCondition.getCompareFull(token)
    }
}
