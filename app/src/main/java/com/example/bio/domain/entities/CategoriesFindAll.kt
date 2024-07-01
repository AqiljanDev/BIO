package com.example.bio.domain.entities

import com.example.bio.domain.entities.findOne.ChildCategory

interface CategoriesFindAll {
    val id: Int
    val id1c: String
    val status: Int
    var slug: String
    val title: String
    val text: String?
    val photo: String?
    val popular: Int
    val parentId: String?
    val discountId: String?
    val discount: Any?
    val childCategory: List<CategoriesFindAll>
}
