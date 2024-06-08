package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.Categories
import com.example.bio.domain.entities.findOne.CharactersToProducts
import com.example.bio.domain.entities.findOne.Product

data class ProductDto (
    override val id: Int,
    override val id1c: String,
    override val slug: String,
    override val article: String,
    override val title: String,
    override val titleFull: String,
    override val status: Int,
    override val price: Int,
    override val count: Int,
    override val desc: String,
    override val youtubeLink: String?,
    override val categoriesId: String,
    override val brandsId: String?,
    override val discount: String?,
    override val gallery: List<String>,
    override val categories: CategoriesDto,
    override val charactersToProducts: List<CharactersToProductsDto>
) : Product
