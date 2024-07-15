package com.example.bio.data.datasource

import com.example.bio.data.api.retrofitProfile
import com.example.bio.domain.entities.findOneOrder.FindOneOrder
import com.example.bio.domain.entities.myOrder.MyOrder
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.domain.repository.ProfileDataSource
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor() : ProfileDataSource {

    override suspend fun getProfileDiscount(token: String): List<UserDiscount> {
        return retrofitProfile.getDiscountUser(token)
    }

    override suspend fun getOrdersFindMy(token: String): List<MyOrder> {
        return retrofitProfile.getFindMy(token)
    }

    override suspend fun getOrdersFinOne(token: String, id: Int): FindOneOrder {
        return retrofitProfile.getFindOne(token, id)
    }
}
