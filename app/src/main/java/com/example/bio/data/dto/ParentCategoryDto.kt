package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.ParentCategory

data class ParentCategoryDto (
    override val slug: String,
    override val title: String,
    override val parentCategory: ParentCategoryDto?,
    override val discount: String?
) : ParentCategory
