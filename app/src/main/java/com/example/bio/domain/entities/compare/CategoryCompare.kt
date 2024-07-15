package com.example.bio.domain.entities.compare

import com.example.bio.domain.entities.findOne.Discount

interface CategoryCompare {
    val discount: Discount?
    val parentCategory: ParentCategoryCompare?
}
