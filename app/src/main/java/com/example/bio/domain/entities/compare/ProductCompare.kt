package com.example.bio.domain.entities.compare

interface ProductCompare {
    val id1c: String
    val title: String
    val slug: String
    val photo: String?
    val count: Int
    val price: Int
    val discount: Any
    val categories: CategoryCompare
    val characters: List<String>
    val categoriesId: String
}
