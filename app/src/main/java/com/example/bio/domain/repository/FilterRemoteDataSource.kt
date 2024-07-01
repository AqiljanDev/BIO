package com.example.bio.domain.repository

import com.example.bio.domain.entities.findOne.Product

interface FilterRemoteDataSource {

    suspend fun getSearchResults(token: String, message: String): List<Product>
}