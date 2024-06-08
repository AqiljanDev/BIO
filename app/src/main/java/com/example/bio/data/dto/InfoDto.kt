package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.ChildCategory
import com.example.bio.domain.entities.findOne.Info
import com.example.bio.domain.entities.findOne.ParentCategory

data class InfoDto (
    override val id: Int,
    override val id1c: String,
    override val title: String,
    override val slug: String,
    override val photo: String,
    override val parentId: String,
    override val parentCategory: ParentCategoryDto?,
    override val childCategory: List<ChildCategoryDto>
) : Info

