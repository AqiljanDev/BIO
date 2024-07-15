package com.example.bio.data.dto.findOneOrder

import com.example.bio.domain.entities.findOneOrder.FindOneOrderUser

data class FindOneOrderUserDto (
    override val id: Int,
    override val email: String,
    override val statusOn: Int,
    override val statusFill: Int,
    override val mainAdmin: Int,
    override val role: String,
    override val password: String,
    override val fio: String?,
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
) : FindOneOrderUser
