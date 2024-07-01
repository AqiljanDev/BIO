package com.example.bio.data.dto.compare

import com.example.bio.domain.entities.compare.CategoryCompare
import com.example.bio.domain.entities.compare.ProductCompare

data class ProductCompareDto (
    override val id1c: String,
    override val title: String,
    override val slug: String,
    override val photo: String?,
    override val count: Int,
    override val price: Int,
    override val discount: Any,
    override val categories: CategoryCompareDto,
    override val characters: List<String>,
    override val categoriesId: String
): ProductCompare
