package com.example.bio.domain.entities.compare

interface CategoryCompare {
    val discount: Any?  // Replace Any? with the correct type if known
    val parentCategory: ParentCategoryCompare?
}
