package com.example.bio.data.dto

data class CreateCheckout(
    val address: String,
    val commentUser: String,
    val userBillId: Int,
    val deliverId: Int,
    val discount: String,
    val products: String
)

data class ProductData(
    val id: Int,
    val c: Int,
    val p: Int,
    val d: String
)

