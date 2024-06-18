package com.example.bio.domain.repository

import com.example.bio.data.dto.ProductDto
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product

interface CatalogRemoteDataSource {
    suspend fun getCatalog(token: String, category: String, page: Int): Catalog

    suspend fun getCollectCharacters(token: String, category: String): CollectCharacter

    suspend fun getSearchResults(token: String, message: String): List<Product>
}