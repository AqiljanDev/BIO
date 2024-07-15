package com.example.bio.domain.repository

import com.example.bio.domain.entities.findOneOrder.FindOneOrder
import com.example.bio.domain.entities.myOrder.MyOrder
import com.example.bio.domain.entities.userDiscount.UserDiscount

interface ProfileDataSource {
    suspend fun getProfileDiscount(token: String): List<UserDiscount>

    suspend fun getOrdersFindMy(token: String): List<MyOrder>

    suspend fun getOrdersFinOne(token: String, id: Int): FindOneOrder
}
