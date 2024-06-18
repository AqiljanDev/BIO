package com.example.bio.domain.useCase

import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.repository.Repository

class GetSearchResultsUseCase(
    private val repository: Repository
) {

    suspend fun execute(token: String, message: String): List<Product> {
        return repository.getSearchResults(token, message)
    }
}