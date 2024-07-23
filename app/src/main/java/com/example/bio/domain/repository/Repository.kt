package com.example.bio.domain.repository

import com.example.bio.domain.entities.Cabinet
import com.example.bio.domain.entities.CategoriesFindAll
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.findOneOrder.FindOneOrder
import com.example.bio.domain.entities.findOneProduct.FindOneProduct
import com.example.bio.domain.entities.myOrder.MyOrder
import com.example.bio.domain.entities.userDiscount.UserDiscount

interface Repository {

    suspend fun getPagingCatalog(token: String, category: String, page: Int): Catalog

    suspend fun getCollectCharacters(token: String, category: String): CollectCharacter

    suspend fun getSearchResults(token: String, message:String): List<Product>

    suspend fun getProductCard(token: String, category: String): FindOneProduct

    suspend fun getCategoriesList(token: String): List<CategoriesFindAll>

    suspend fun getCatalogFilter(
        token: String,
        category: String,
        min: Int?,
        max: Int?,
        sort: String,
        chars: String,
        page: Int
    ): Catalog

    suspend fun getProfileDiscount(token: String): List<UserDiscount>

    suspend fun getOrdersFindMy(token: String): List<MyOrder>

    suspend fun getOrdersFindOne(token: String, id: Int): FindOneOrder

    suspend fun getCabinetFindMy(token: String): Cabinet

    suspend fun putCabinetUpdate(token: String, cabinet: Cabinet): Cabinet
}