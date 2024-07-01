package com.example.bio.data.dto

data class OrderDetails(
    val id: Int,
    val address: String,
    val commentUser: String,
    val commentAdmin: String?, // nullable, так как может быть null в JSON
    val date: String,
    val userBillId: Int,
    val deliverId: Int,
    val orderStatusId: Int,
    val usersId: Int,
    val adminId: Int? // nullable, так как может быть null в JSON
)

