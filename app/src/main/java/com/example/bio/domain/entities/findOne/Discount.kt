package com.example.bio.domain.entities.findOne

interface Discount {
    val id: Int
    val type: Int
    val value: Int
    val userId: String?
    val userCatId: String?
    val productId: String?
    val categoryId: String?
}
