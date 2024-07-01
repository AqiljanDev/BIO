package com.example.bio.presentation.data

data class CustomCompare(
    val id: Int = 0,
    val id1c: String = "",
    val title: String = "",
    val slug: String = "",
    val photo: String? = null,
    val count: Int = 0,
    val price: Int = 0,
    val characters: List< String>
)
