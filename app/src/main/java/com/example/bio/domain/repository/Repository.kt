package com.example.bio.domain.repository

import androidx.paging.Pager
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product

interface Repository {

    suspend fun getPagingCatalog(token: String, category: String, page: Int): Catalog

    suspend fun getCollectCharacters(token: String, category: String): CollectCharacter

    suspend fun getSearchResults(token: String, message:String): List<Product>
}