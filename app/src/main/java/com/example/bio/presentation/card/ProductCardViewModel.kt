package com.example.bio.presentation.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.findOneProduct.FindOneProduct
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.bio.domain.useCase.DeleteCartUseCase
import com.example.bio.domain.useCase.GetMiniCartUseCase
import com.example.bio.domain.useCase.GetMiniCompareUseCase
import com.example.bio.domain.useCase.GetMiniWishListGetUseCase
import com.example.bio.domain.useCase.GetProductCardUseCase
import com.example.bio.domain.useCase.GetProfileDiscountUseCase
import com.example.bio.domain.useCase.PostCartEventUseCase
import com.example.bio.domain.useCase.PostCompareEventUseCase
import com.example.bio.domain.useCase.PostWishListEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCardViewModel @Inject constructor(
    private val getProductCardUseCase: GetProductCardUseCase,
    private val getMiniWishListGetUseCase: GetMiniWishListGetUseCase,
    private val getMiniCompareUseCase: GetMiniCompareUseCase,
    private val getMiniCartUseCase: GetMiniCartUseCase,
    private val postWishListEventUseCase: PostWishListEventUseCase,
    private val postCompareEventUseCase: PostCompareEventUseCase,
    private val postCartEventUseCase: PostCartEventUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getProfileDiscountUseCase: GetProfileDiscountUseCase
) : ViewModel() {
    private val _product: MutableSharedFlow<FindOneProduct> = MutableSharedFlow()
    val product get() = _product.asSharedFlow()

    private val _wishListMini: MutableSharedFlow<List<WishListCompareMini>> = MutableSharedFlow()
    val wishListMini get() = _wishListMini.asSharedFlow()

    private val _compareMini: MutableSharedFlow<List<WishListCompareMini>> = MutableSharedFlow()
    val compareMini get() = _compareMini.asSharedFlow()

    private val _cartMini: MutableSharedFlow<CartMini> = MutableSharedFlow()
    val cartMini get() = _cartMini.asSharedFlow()

    private val _profileDiscount: MutableStateFlow<List<UserDiscount>> = MutableStateFlow(listOf())
    val profileDiscount get() = _profileDiscount.asStateFlow()


    fun getProductCard(token: String, category: String) {
        viewModelScope.launch {
            val res = getProductCardUseCase.execute(
                token, category
            )

            _product.emit(res)
            getWishListMini(token)
            getCompareMini(token)
            getCartMini(token)
            getProfileDiscount(token)
        }
    }

    private fun getProfileDiscount(token: String) {
        viewModelScope.launch {
            val res = getProfileDiscountUseCase.execute(token)

            _profileDiscount.emit(res)
        }
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