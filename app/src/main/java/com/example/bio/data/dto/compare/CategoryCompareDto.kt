package com.example.bio.data.dto.compare

import com.example.bio.domain.entities.compare.CategoryCompare
import com.example.bio.domain.entities.compare.ParentCategoryCompare

data class CategoryCompareDto (
    override val discount: Any?,
    override val parentCategory: ParentCategoryCompareDto?
): CategoryCompare
