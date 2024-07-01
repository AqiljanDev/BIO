package com.example.bio.data.dto

import com.example.bio.domain.entities.FindOneBrand
import com.example.bio.domain.entities.InfoBrand
import com.example.bio.domain.entities.findOne.Product

data class FindOneBrandDto (
    override val info: InfoBrandDto,
    override val products: List<ProductDto>
) : FindOneBrand