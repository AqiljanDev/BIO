package com.example.bio.data.dto.compare

import com.example.bio.domain.entities.compare.ParentCategoryCompare

data class ParentCategoryCompareDto (
    override val slug: String,
    override val title: String,
    override val parentCategory: ParentCategoryCompareDto?,
    override val discount: Any?
): ParentCategoryCompare
