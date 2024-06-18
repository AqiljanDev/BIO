package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.GalleryItem

data class GalleryItemDto (
    override val id: Int,
    override val photo: String,
    override val order: Int,
    override val productsId: String
) : GalleryItem
