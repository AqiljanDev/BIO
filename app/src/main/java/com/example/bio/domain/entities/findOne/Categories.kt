package com.example.bio.domain.entities.findOne

interface Categories {
    val id: Int
    val id1c: String
    val status: Int
    val slug: String
    val title: String
    val text: String?
    val photo: String?
    val popular: Int
    val parentId: String
    val discountId: String?
    val discount: Discount?
    val parentCategory: ParentCategory?
}
