package com.example.bio.data.dto

import com.example.bio.domain.entities.cart.ProductMiniCard

data class ProductMiniCardDto(
    override val id: Int,
    override val id1c: Int,
    override val prodId: String,
    override val count: Int
): ProductMiniCard
