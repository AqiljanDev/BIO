package com.example.bio.data.dto.myOrder

import com.example.bio.domain.entities.myOrder.MyOrderProduct

data class MyOrderProductDto (
    override val id: Int,
    override val price: Int,
    override val count: Int
) : MyOrderProduct
