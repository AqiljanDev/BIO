package com.example.bio.domain.useCase

import com.example.bio.data.repository.RepositoryImpl
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class GetCatalogFilterUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend fun execute(
        token: String,
        category: String,
        min: Int?,
        max: Int?,
        sort: String,
        chars: String,
        page: Int
    ): Catalog {
        return repository.getCatalogFilter(token, category, min, max, sort, chars, page)
    }
}
