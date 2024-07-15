package com.example.bio.data.dto.user

import com.example.bio.data.dto.DiscountDto
import com.example.bio.domain.entities.userDiscount.UserChildCategory

data class UserChildCategoryDto (
    override val id1c: String,
    override val slug: String,
    override val title: String,
    override val childCategory: List<UserChildCategoryDto>,
    override val discount: DiscountDto?
) : UserChildCategory
