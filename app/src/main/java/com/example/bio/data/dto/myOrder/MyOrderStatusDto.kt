package com.example.bio.data.dto.myOrder

import com.example.bio.domain.entities.myOrder.MyOrderStatus

data class MyOrderStatusDto (
    override val id: Int,
    override val name: String,
    override val default: Int,
    override val endStatus: Int
) : MyOrderStatus
