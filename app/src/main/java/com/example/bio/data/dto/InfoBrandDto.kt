package com.example.bio.data.dto

import com.example.bio.domain.entities.InfoBrand

data class InfoBrandDto (
    override val id: Int,
    override val id1c: String,
    override val title: String,
    override val slug: String,
    override val photo: String
) : InfoBrand
