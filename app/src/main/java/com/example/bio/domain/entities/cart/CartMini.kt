package com.example.bio.domain.entities.cart

interface CartMini {
    val products: List<ProductMiniCard>
    val totalCount: Int
}