package com.example.bio.data.dto

import com.example.bio.domain.entities.UpdateBills

data class UpdateBillsDto(
    override val code: String,
    override val bank: String,
    override val kbe: String
) : UpdateBills
