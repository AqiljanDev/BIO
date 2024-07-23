package com.example.bio.data.dto.findOneOrder

import com.example.bio.domain.entities.findOneOrder.FindOneOrderUserBill

data class FindOneOrderUserBillDto (
    override val id: Int,
    override var bank: String,
    override var code: String,
    override var kbe: String,
    override val status: Int,
    override val usersId: Int
) : FindOneOrderUserBill
