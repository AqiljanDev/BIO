package com.example.bio.domain.repository

import com.example.bio.data.dto.CatalogDto
import com.example.bio.data.dto.FindOneProductDto
import com.example.bio.domain.entities.findOne.Product

interface FilterRemoteDataSource {

    suspend fun getSearchResults(token: String, message: String): List<Product>

    suspend fun getFindOneFilter(
        token: String,
        category: String,
        min: Int?,
        max: Int?,
        sort: String,
        chars: String,
        page: Int
    ): CatalogDto
}