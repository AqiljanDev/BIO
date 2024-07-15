package com.example.bio.data.dto.user

import com.example.bio.domain.entities.userDiscount.UserDiscount

data class UserDiscountDto(
    override val id: Int,
    override val type: Int,
    override val value: Int,
    override val userId: Int,
    override val userCatId: String,
    override val productId: String?,
    override val categoryId: String?,
    override val userCategory: UserCategoryDto?
) : UserDiscount
