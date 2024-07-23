package com.example.bio.data.api

import com.example.bio.data.dto.CabinetDto
import com.example.bio.data.dto.UpdateBillsDto
import com.example.bio.data.dto.findOneOrder.FindOneOrderDto
import com.example.bio.data.dto.findOneOrder.FindOneOrderUserBillDto
import com.example.bio.data.dto.myOrder.MyOrderDto
import com.example.bio.data.dto.user.UserDiscountDto
import com.example.bio.domain.entities.Cabinet
import com.example.bio.domain.entities.UpdateBills
import com.example.bio.domain.entities.findOneOrder.FindOneOrderUserBill
import com.example.core.UrlConstants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


val retrofitProfile: ProfileService by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProfileService::class.java)
}

interface ProfileService {

    @GET("discount")
    suspend fun getDiscountUser(
        @Header("Authorization") token: String
    ): List<UserDiscountDto>

    @GET("orders/my")
    suspend fun getFindMy(
        @Header("Authorization") token: String
    ): List<MyOrderDto>

    @GET("orders/{id}")
    suspend fun getFindOne(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): FindOneOrderDto

    @GET("cabinet")
    suspend fun getCabinetFindMy(
        @Header("Authorization") token: String
    ): CabinetDto

    @PUT("cabinet")
    suspend fun putCabinetUpdate(
        @Header("Authorization") token: String,
        @Body cabinet: CabinetDto
    ): CabinetDto


    @GET("bills")
    suspend fun getBillMy(
        @Header("Authorization") token: String
    ): List<FindOneOrderUserBillDto>

    @POST("bills")
    suspend fun postBillCreate(
        @Header("Authorization") token: String,
        @Body updateBills: UpdateBillsDto
    ): FindOneOrderUserBillDto

    @PUT("bills/{id}")
    suspend fun putBillUpdate(
        @Header("Authorization") token: String,
        @Body updateBills: UpdateBillsDto,
        @Path("id") id: Int
    ): FindOneOrderUserBillDto

    @DELETE("bills/{id}")
    suspend fun deleteBill(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    )

}