package com.example.bio.data.dto

data class WishListFullDto (
    val id: Int,
    val userId: Int,
    val prodId: String,
    val product: ProductDto
)
