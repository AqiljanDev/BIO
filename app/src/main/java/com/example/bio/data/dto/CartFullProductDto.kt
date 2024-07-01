package com.example.bio.data.dto

import com.example.bio.domain.entities.cart.CartFullProduct
import com.example.bio.domain.entities.findOne.Product

data class CartFullProductDto (
    override val id: Int,
    override val userId: Int,
    override val prodId: String,
    override val count: Int,
    override val product: ProductDto
) : CartFullProduct
