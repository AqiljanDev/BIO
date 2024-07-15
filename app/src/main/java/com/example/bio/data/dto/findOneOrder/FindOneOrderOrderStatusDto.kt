package com.example.bio.data.dto.findOneOrder

import com.example.bio.domain.entities.findOneOrder.FindOneOrderOrderStatus

data class FindOneOrderOrderStatusDto (
    override val id: Int,
    override val name: String,
    override val default: Int,
    override val endStatus: Int
) : FindOneOrderOrderStatus
