package com.example.bio.domain.entities.findOneOrder

interface FindOneOrderUserBill {
    val id: Int
    var bank: String
    var code: String
    var kbe: String
    val status: Int
    val usersId: Int
}
