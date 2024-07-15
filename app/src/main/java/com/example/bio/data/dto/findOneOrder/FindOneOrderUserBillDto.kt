package com.example.bio.data.dto.findOneOrder

import com.example.bio.domain.entities.findOneOrder.FindOneOrderUserBill

class FindOneOrderUserBillDto (
    override val id: Int,
    override val bank: String,
    override val code: String,
    override val kbe: String,
    override val status: Int,
    override val usersId: Int
) : FindOneOrderUserBill
