package com.example.bio.domain.entities.findOne

interface ParentCategory {
    val slug: String
    val title: String
    val parentCategory: ParentCategory?
    val discount: String?
}