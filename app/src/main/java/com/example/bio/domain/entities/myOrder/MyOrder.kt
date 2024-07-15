package com.example.bio.domain.entities.myOrder

interface MyOrder {
    val id: Int
    val address: String
    val commentUser: String
    val commentAdmin: String?
    val date: String
    val userBillId: Int
    val deliverId: Int
    val orderStatusId: Int
    val usersId: Int
    val adminId: String?
    val userBill: MyOrderUserBill
    val orderStatus: MyOrderStatus
    val products: List<MyOrderProduct>
}
