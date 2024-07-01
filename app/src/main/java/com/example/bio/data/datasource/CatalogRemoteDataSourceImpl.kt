package com.example.bio.data.datasource

import android.util.Log
import com.example.bio.data.api.CatalogService
import com.example.bio.data.api.retrofitCatalog
import com.example.bio.data.api.retrofitProductCard
import com.example.bio.data.api.retrofitSearchAndFilter
import com.example.bio.data.dto.CatalogDto
import com.example.bio.data.dto.CategoriesFindAllDto
import com.example.bio.data.dto.ProductDto
import com.example.bio.domain.entities.CategoriesFindAll
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.findOneProduct.FindOneProduct
import com.example.bio.domain.repository.CatalogRemoteDataSource
import javax.inject.Inject

class CatalogRemoteDataSourceImpl @Inject constructor() : CatalogRemoteDataSource {
    override suspend fun getCatalog(token: String, category: String, page: Int): CatalogDto {
        return retrofitCatalog.findOne(token, category, page)
    }

    override suspend fun getCollectCharacters(token: String, category: String): CollectCharacter {
        return retrofitCatalog.collectCharacters(token, category)
    }

    override suspend fun getProductCard(token: String, category: String): FindOneProduct {
        return retrofitProductCard.findOneProduct(token, category)
    }

    override suspend fun getCategoriesFinAll(token: String): List<CategoriesFindAllDto> {
        return retrofitCatalog.categoriesFindAll(token)
    }

}

