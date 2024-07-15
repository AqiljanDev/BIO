package com.example.bio.domain.entities.findOneOrder

interface FindOneOrder {
    val id: Int
    val address: String
    val commentUser: String
    val commentAdmin: String?
    val date: String
    val userBillId: Int
    val deliverId: Int
    val orderStatusId: Int
    val usersId: Int
    val adminId: Int?
    val products: List<FindOneOrderProduct>
    val orderStatus: FindOneOrderOrderStatus
    val users: FindOneOrderUser
    val userBill: FindOneOrderUserBill
    val admins: FindOneOrderAdmin?
}