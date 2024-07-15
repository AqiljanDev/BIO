package com.example.bio.domain.entities.userDiscount

import com.example.bio.domain.entities.findOne.Discount

interface UserChildCategory {
    val id1c: String
    val slug: String
    val title: String
    val childCategory: List<UserChildCategory>
    val discount: Discount?
}
