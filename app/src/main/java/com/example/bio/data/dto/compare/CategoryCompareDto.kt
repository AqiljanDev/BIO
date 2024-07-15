package com.example.bio.data.dto.compare

import com.example.bio.data.dto.DiscountDto
import com.example.bio.domain.entities.compare.CategoryCompare

data class CategoryCompareDto (
    override val discount: DiscountDto?,
    override val parentCategory: ParentCategoryCompareDto?
): CategoryCompare
