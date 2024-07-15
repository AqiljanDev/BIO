package com.example.bio.domain.entities.compare

import com.example.bio.domain.entities.findOne.Discount

interface ParentCategoryCompare {
    val slug: String
    val title: String
    val parentCategory: ParentCategoryCompare?
    val discount: Discount?
}
