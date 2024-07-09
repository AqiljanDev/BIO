package com.example.bio.domain.repository

import androidx.paging.Pager
import com.example.bio.data.dto.CategoriesFindAllDto
import com.example.bio.domain.entities.CategoriesFindAll
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.findOneProduct.FindOneProduct

interface Repository {

    suspend fun getPagingCatalog(token: String, category: String, page: Int): Catalog

    suspend fun getCollectCharacters(token: String, category: String): CollectCharacter

    suspend fun getSearchResults(token: String, message:String): List<Product>

    suspend fun getProductCard(token: String, category: String): FindOneProduct

    suspend fun getCategoriesList(token: String): List<CategoriesFindAll>

    suspend fun getCatalogFilter(
        token: String,
        category: String,
        min: Int?,
        max: Int?,
        sort: String,
        chars: String,
        page: Int
    ): Catalog
}