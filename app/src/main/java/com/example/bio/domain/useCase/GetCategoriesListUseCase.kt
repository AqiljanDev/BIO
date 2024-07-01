package com.example.bio.domain.useCase

import com.example.bio.domain.entities.CategoriesFindAll
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class GetCategoriesListUseCase @Inject constructor(
    private val repository: Repository
){

    suspend fun execute(token: String): List<CategoriesFindAll> {
        return repository.getCategoriesList(token)
    }
}