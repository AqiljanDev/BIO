package com.example.bio.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.data.api.retrofitProductCondition
import com.example.bio.data.dto.WishListFullDto
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.bio.domain.useCase.DeleteCartUseCase
import com.example.bio.domain.useCase.GetCatalogUseCase
import com.example.bio.domain.useCase.GetMiniCartUseCase
import com.example.bio.domain.useCase.GetMiniCompareUseCase
import com.example.bio.domain.useCase.GetMiniWishListGetUseCase
import com.example.bio.domain.useCase.PostCartEventUseCase
import com.example.bio.domain.useCase.PostCompareEventUseCase
import com.example.bio.domain.useCase.PostWishListEventUseCase
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getMiniWishListGetUseCase: GetMiniWishListGetUseCase,
    private val getMiniCompareUseCase: GetMiniCompareUseCase,
    private val getMiniCartUseCase: GetMiniCartUseCase,
    private val postWishListEventUseCase: PostWishListEventUseCase,
    private val postCompareEventUseCase: PostCompareEventUseCase,
    private val postCartEventUseCase: PostCartEventUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
) : ViewModel() {

    private val _fullProductList: MutableStateFlow<List<WishListFullDto>> = MutableStateFlow(listOf())
    val fullProductList get() = _fullProductList.asStateFlow()

    private val _wishListMini: MutableSharedFlow<List<WishListCompareMini>> = MutableSharedFlow()
    val wishListMini get() = _wishListMini.asSharedFlow()

    private val _compareMini: MutableSharedFlow<List<WishListCompareMini>> = MutableSharedFlow()
    val compareMini get() = _compareMini.asSharedFlow()

    private val _cartMini: MutableSharedFlow<CartMini> = MutableSharedFlow()
    val cartMini get() = _cartMini.asSharedFlow()

    fun getProducts(token: String) {
        viewModelScope.launch {
            val res = retrofitProductCondition.getFullWishList(token)
            _fullProductList.emit(res)
        }

        getWishListMini(token)
        getCompareMini(token)
        getCartMini(token)
    }

    private fun getWishListMini(token: String) {
        viewModelScope.launch {
            val res = getMiniWishListGetUseCase.execute(token)
            _wishListMini.emit(res)
        }
    }

    private fun getCompareMini(token: String) {
        viewModelScope.launch {
            val res = getMiniCompareUseCase.execute(token)
            _compareMini.emit(res)
        }
    }

    private fun getCartMini(token: String) {
        viewModelScope.launch {
            val res = getMiniCartUseCase.execute(token)
            _cartMini.emit(res)
        }
    }

    fun eventWishList(token: String, id1c: String) {
        viewModelScope.launch {
            val res = postWishListEventUseCase.execute(token, id1c)
            _wishListMini.emit(res)
        }
    }

    fun eventCompare(token: String, id1c: String) {
        viewModelScope.launch {
            val res = postCompareEventUseCase.execute(token, id1c)
            _compareMini.emit(res)
        }
    }

    fun eventCart(token: String, postCart: PostCart) {
        viewModelScope.launch {
            val res = postCartEventUseCase.execute(token, postCart)
            _cartMini.emit(res)
        }
    }

    fun deleteCart(token: String, id: Int) {
        viewModelScope.launch {
            val res = deleteCartUseCase.execute(token, id)
            _cartMini.emit(res)
        }
    }
}