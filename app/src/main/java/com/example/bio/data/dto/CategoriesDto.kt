package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.Categories
import com.example.bio.domain.entities.findOne.ParentCategory

data class CategoriesDto (
    override val id: Int,
    override val id1c: String,
    override val status: Int,
    override val slug: String,
    override val title: String,
    override val text: String?,
    override val photo: String?,
    override val popular: Int,
    override val parentId: String,
    override val discountId: String?,
    override val discount: String?,
    override val parentCategory: ParentCategoryDto?
) : Categories