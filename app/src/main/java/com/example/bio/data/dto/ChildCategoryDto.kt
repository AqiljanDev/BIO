package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.ChildCategory

data class ChildCategoryDto (
    override val title: String,
    override val slug: String
) : ChildCategory
