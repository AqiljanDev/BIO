package com.example.bio.presentation.data

import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.domain.entities.wishList.WishListCompareMini

data class QuadCatalog(
    val products: Catalog,
    val wishList: List<WishListCompareMini>,
    val compareList: List<WishListCompareMini>,
    val cart: CartMini,
    val profile: List<UserDiscount>
)

