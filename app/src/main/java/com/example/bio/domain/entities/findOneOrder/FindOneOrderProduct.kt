package com.example.bio.domain.entities.findOneOrder

interface FindOneOrderProduct {
    val id: Int
    val id1c: String
    val productId: Int
    val article: String
    val title: String
    val preview: FindOneOrderProductPreview?
    val discount: String
    val price: Int
    val count: Int
    val orderId: Int
}
