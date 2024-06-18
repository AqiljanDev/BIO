package com.example.bio.data

import com.example.bio.data.dto.CatalogDto
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.repository.CatalogRemoteDataSource
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: CatalogRemoteDataSource
) : Repository {
    override suspend fun getPagingCatalog(
        token: String,
        category: String,
        page: Int
    ): Catalog {
        return remoteDataSource.getCatalog(token, category, page)
    }

    override suspend fun getCollectCharacters(
        token: String,
        category: String
    ): CollectCharacter {
        return remoteDataSource.getCollectCharacters(token, category)
    }

    override suspend fun getSearchResults(token: String, message: String): List<Product> {
        return remoteDataSource.getSearchResults(token, message)
    }

}