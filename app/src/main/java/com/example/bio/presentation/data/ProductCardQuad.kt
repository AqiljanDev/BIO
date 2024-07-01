package com.example.bio.presentation.data

import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.findOneProduct.FindOneProduct
import com.example.bio.domain.entities.wishList.WishListCompareMini

data class ProductCardQuad(
    val product: FindOneProduct,
    val wishList: List<WishListCompareMini>,
    val compareList: List<WishListCompareMini>,
    val cart: CartMini
)