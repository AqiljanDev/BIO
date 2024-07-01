package com.example.bio.data.dto

import com.example.bio.domain.entities.CategoriesFindAll
import com.example.bio.domain.entities.findOne.ChildCategory

data class CategoriesFindAllDto(
    override val id: Int,
    override val id1c: String,
    override val status: Int,
    override var slug: String,
    override val title: String,
    override val text: String?,
    override val photo: String?,
    override val popular: Int,
    override val parentId: String?,
    override val discountId: String?,
    override val discount: Any?,
    override val childCategory: List<CategoriesFindAllDto>
) : CategoriesFindAll
