package com.example.bio.data.dto.myOrder

import com.example.bio.domain.entities.myOrder.MyOrderUserBill

data class MyOrderUserBillDto (
    override val id: Int,
    override val bank: String,
    override val code: String,
    override val kbe: String,
    override val status: Int,
    override val usersId: Int
) : MyOrderUserBill
