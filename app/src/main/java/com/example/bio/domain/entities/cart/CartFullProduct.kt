package com.example.bio.domain.entities.cart

import com.example.bio.domain.entities.findOne.Product

interface CartFullProduct {
    val id: Int
    val userId: Int
    val prodId: String
    val count: Int
    val product: Product
}