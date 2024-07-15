package com.example.bio.data.dto.findOneOrder

import com.example.bio.domain.entities.findOneOrder.FindOneOrder

data class FindOneOrderDto(
    override val id: Int,
    override val address: String,
    override val commentUser: String,
    override val commentAdmin: String?,
    override val date: String,
    override val userBillId: Int,
    override val deliverId: Int,
    override val orderStatusId: Int,
    override val usersId: Int,
    override val adminId: Int?,
    override val products: List<FindOneOrderProductSDto>,
    override val orderStatus: FindOneOrderOrderStatusDto,
    override val users: FindOneOrderUserDto,
    override val userBill: FindOneOrderUserBillDto,
    override val admins: FindOneOrderAdminDto?
) : FindOneOrder

