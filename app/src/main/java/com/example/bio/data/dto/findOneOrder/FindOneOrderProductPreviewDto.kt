package com.example.bio.data.dto.findOneOrder

import com.example.bio.domain.entities.findOneOrder.FindOneOrderProductPreview

data class FindOneOrderProductPreviewDto (
    override val type: String,
    override val data: List<Int>
) : FindOneOrderProductPreview
