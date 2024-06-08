package com.example.bio.data.dto

import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Info
import com.example.bio.domain.entities.findOne.Product

data class CatalogDto(
    override val info: InfoDto,
    override val products: List<ProductDto>
) : Catalog
