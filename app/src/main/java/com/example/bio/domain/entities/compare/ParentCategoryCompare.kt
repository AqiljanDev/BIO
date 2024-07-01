package com.example.bio.domain.entities.compare

interface ParentCategoryCompare {
    val slug: String
    val title: String
    val parentCategory: ParentCategoryCompare?
    val discount: Any?
}
