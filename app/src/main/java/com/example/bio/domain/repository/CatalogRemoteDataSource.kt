package com.example.bio.domain.repository

import com.example.bio.data.dto.ProductDto
import com.example.bio.domain.entities.CategoriesFindAll
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.findOneProduct.FindOneProduct

interface CatalogRemoteDataSource {
    suspend fun getCatalog(token: String, category: String, page: Int): Catalog

    suspend fun getCollectCharacters(token: String, category: String): CollectCharacter

    suspend fun getProductCard(token: String, category: String): FindOneProduct

    suspend fun getCategoriesFinAll(token: String): List<CategoriesFindAll>
}