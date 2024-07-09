package com.example.bio.data.datasource

import com.example.bio.data.api.retrofitSearchAndFilter
import com.example.bio.data.dto.CatalogDto
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.repository.FilterRemoteDataSource
import javax.inject.Inject

class FilterRemoteDataSourceImpl @Inject constructor() : FilterRemoteDataSource {
    override suspend fun getSearchResults(token: String, message: String): List<Product> {
        return retrofitSearchAndFilter.search(token, message)
    }

    override suspend fun getFindOneFilter(
        token: String,
        category: String,
        min: Int?,
        max: Int?,
        sort: String,
        chars: String,
        page: Int
    ): CatalogDto {
        return retrofitSearchAndFilter.findOne(token, category, min, max, sort, chars, page)
    }
}
