package com.example.bio.domain.entities.findOne

import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.presentation.data.PriceDiscount

interface Product {
    val id: Int
    val id1c: String
    val slug: String
    val article: String
    val title: String
    val titleFull: String
    val status: Int
    val price: Int
    val count: Int
    val desc: String
    val youtubeLink: String?
    val categoriesId: String
    val brandsId: String?
    val discount: Discount?
    val gallery: List<GalleryItem>
    val categories: Categories
    val charactersToProducts: List<CharactersToProducts>

    fun discountPrice(listProfileDiscount: List<UserDiscount>): PriceDiscount
}
