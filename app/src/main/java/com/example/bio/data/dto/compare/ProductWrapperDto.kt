package com.example.bio.data.dto.compare

import com.example.bio.domain.entities.compare.ProductCompare
import com.example.bio.domain.entities.compare.ProductWrapper

data class ProductWrapperDto (
    override val id: Int,
    override val product: ProductCompareDto
): ProductWrapper
