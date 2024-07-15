package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.Discount

data class DiscountDto (
    override val id: Int,
    override val type: Int,
    override val value: Int,
    override val userId: String?,
    override val userCatId: String?,
    override val productId: String?,
    override val categoryId: String?
) : Discount
