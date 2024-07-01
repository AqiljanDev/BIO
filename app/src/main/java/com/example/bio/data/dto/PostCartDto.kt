package com.example.bio.data.dto

import com.example.bio.domain.entities.cart.PostCart

data class PostCartDto (
    override val prodId: String,
    override val count: Int
): PostCart
