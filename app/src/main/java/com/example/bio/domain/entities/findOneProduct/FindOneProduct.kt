package com.example.bio.domain.entities.findOneProduct

import com.example.bio.domain.entities.findOne.Product

interface FindOneProduct : Product {
    val brands: Brands?
    val files: List<String>
    val other: List<Product>
}