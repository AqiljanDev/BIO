package com.example.bio.data.repository

import com.example.bio.data.dto.CatalogDto
import com.example.bio.domain.entities.CategoriesFindAll
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.findOneProduct.FindOneProduct
import com.example.bio.domain.repository.CatalogRemoteDataSource
import com.example.bio.domain.repository.FilterRemoteDataSource
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val catalogDataSource: CatalogRemoteDataSource,
    private val filterDataSource: FilterRemoteDataSource
) : Repository {
    override suspend fun getPagingCatalog(
        token: String,
        category: String,
        page: Int
    ): Catalog {
        return catalogDataSource.getCatalog(token, category, page)
    }

    override suspend fun getCollectCharacters(token: String, category: String): CollectCharacter {
        return catalogDataSource.getCollectCharacters(token, category)
    }

    override suspend fun getSearchResults(token: String, message: String): List<Product> {
        return filterDataSource.getSearchResults(token, message)
    }

    override suspend fun getProductCard(token: String, category: String): FindOneProduct {
        return catalogDataSource.getProductCard(token, category)
    }

    override suspend fun getCategoriesList(token: String): List<CategoriesFindAll> {
        return catalogDataSource.getCategoriesFinAll(token)
    }

    override suspend fun getCatalogFilter(
        token: String,
        category: String,
        min: Int?,
        max: Int?,
        sort: String,
        chars: String,
        page: Int
    ): Catalog {
        return filterDataSource.getFindOneFilter(token, category, min, max, sort, chars, page)
    }

}