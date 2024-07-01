package com.example.bio.presentation.data

import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.compare.CompareFull

data class Duo(
    val compare: CompareFull,
    val cartMini: CartMini
)
