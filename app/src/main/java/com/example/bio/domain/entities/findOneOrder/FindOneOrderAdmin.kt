package com.example.bio.domain.entities.findOneOrder

interface FindOneOrderAdmin {
    val id: Int
    val email: String
    val statusOn: Int
    val statusFill: Int
    val mainAdmin: Int
    val role: String
    val password: String
    val fio: String?
    val phone: String
    val company: String
    val type: String
    val area: String
    val site: String
    val bin: String
    val address: String
    val bik: String
    val bank: String
    val iik: String
}
