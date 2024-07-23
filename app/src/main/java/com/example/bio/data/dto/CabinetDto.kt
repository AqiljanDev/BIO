package com.example.bio.data.dto

import com.example.bio.domain.entities.Cabinet

data class CabinetDto(
    override val phone: String,
    override val company: String,
    override val type: String,
    override val area: String,
    override val site: String,
    override val bin: String,
    override val address: String,
    override val bik: String,
    override val bank: String,
    override val iik: String
) : Cabinet
