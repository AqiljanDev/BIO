package com.example.bio.presentation.data

import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.wishList.WishListCompareMini

data class Quad(
    val catalog: List<Product>,
    val wishList: List<WishListCompareMini>,
    val compareList: List<WishListCompareMini>,
    val cart: CartMini
)
