package com.example.bio.domain.entities.cart

interface CartFull {
    val products: List<CartFullProduct>
    val totalCount: Int
}