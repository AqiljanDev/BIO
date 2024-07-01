package com.example.bio.data.datasource

import com.example.bio.data.api.retrofitSearchAndFilter
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.repository.FilterRemoteDataSource
import javax.inject.Inject

class FilterRemoteDataSourceImpl @Inject constructor() : FilterRemoteDataSource {
    override suspend fun getSearchResults(token: String, message: String): List<Product> {
        return retrofitSearchAndFilter.search(token, message)
    }
}