package com.example.bio.data.dto

import com.example.bio.domain.entities.cart.CartMini

data class CartMiniDto(
    override val products: List<ProductMiniCardDto>,
    override val totalCount: Int
): CartMini
