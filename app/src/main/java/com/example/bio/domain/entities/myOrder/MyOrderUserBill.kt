package com.example.bio.domain.entities.myOrder

interface MyOrderUserBill {
    val id: Int
    val bank: String
    val code: String
    val kbe: String
    val status: Int
    val usersId: Int
}
