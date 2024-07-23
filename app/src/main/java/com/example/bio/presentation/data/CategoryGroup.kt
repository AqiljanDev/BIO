package com.example.bio.presentation.data

import com.example.bio.domain.entities.compare.CharactersToCompare
import com.example.bio.domain.entities.compare.ProductWrapper

data class CategoryGroupFull(
    val categoryGroups: List<CategoryGroup>
) {
    data class CategoryGroup(
        val categoryId: String,
        val products: List<ProductWrapper>,
        val characters: List<CharactersToCompare>
    )
}

