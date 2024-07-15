package com.example.bio.data.dto.findOneOrder

import com.example.bio.domain.entities.findOneOrder.FindOneOrderProduct

data class FindOneOrderProductSDto (
    override val id: Int,
    override val id1c: String,
    override val productId: Int,
    override val article: String,
    override val title: String,
    override val preview: FindOneOrderProductPreviewDto?,
    override val discount: String,
    override val price: Int,
    override val count: Int,
    override val orderId: Int
) : FindOneOrderProduct
