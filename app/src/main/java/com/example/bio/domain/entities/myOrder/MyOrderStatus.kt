package com.example.bio.domain.entities.myOrder

interface MyOrderStatus {
    val id: Int
    val name: String
    val default: Int
    val endStatus: Int
}
