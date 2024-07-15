package com.example.bio.data.dto.myOrder

import com.example.bio.domain.entities.myOrder.MyOrder

data class MyOrderDto (
    override val id: Int,
    override val address: String,
    override val commentUser: String,
    override val commentAdmin: String?,
    override val date: String,
    override val userBillId: Int,
    override val deliverId: Int,
    override val orderStatusId: Int,
    override val usersId: Int,
    override val adminId: String?,
    override val userBill: MyOrderUserBillDto,
    override val orderStatus: MyOrderStatusDto,
    override val products: List<MyOrderProductDto>
) : MyOrder