package com.example.bio.domain.useCase

import android.util.Log
import androidx.paging.Pager
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.repository.Repository
import javax.inject.Inject

class GetCatalogUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend fun execute(token: String, category: String, page: Int): Catalog {
        return repository.getPagingCatalog(token, category, page)
    }
}
