package com.example.bio.data.dto

import com.example.bio.domain.entities.cart.CartFull
import com.example.bio.domain.entities.cart.CartFullProduct

data class CartFullDto(
    override val products: List<CartFullProductDto>,
    override val totalCount: Int
): CartFull
