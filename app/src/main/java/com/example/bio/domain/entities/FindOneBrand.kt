package com.example.bio.domain.entities

import com.example.bio.domain.entities.findOne.Product

interface FindOneBrand {
    val info: InfoBrand
    val products: List<Product>
}