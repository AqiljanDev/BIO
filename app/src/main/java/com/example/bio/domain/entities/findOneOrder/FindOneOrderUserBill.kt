package com.example.bio.domain.entities.findOneOrder

interface FindOneOrderUserBill {
    val id: Int
    val bank: String
    val code: String
    val kbe: String
    val status: Int
    val usersId: Int
}
