package com.example.bio.domain.entities.compare

import com.example.bio.domain.entities.findOne.Discount
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.presentation.data.PriceDiscount

interface ProductCompare {
    val id1c: String
    val title: String
    val slug: String
    val photo: String?
    val count: Int
    val price: Int
    val discount: Discount?
    val categories: CategoryCompare
    val characters: List<String>
    val categoriesId: String

    fun discountPrice(listProfileDiscount: List<UserDiscount>): PriceDiscount
}
