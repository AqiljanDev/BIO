package com.example.bio.presentation.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.data.api.retrofitProductCondition
import com.example.bio.data.dto.CreateCheckout
import com.example.bio.domain.entities.cart.CartFull
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.findOneOrder.FindOneOrderUserBill
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.domain.useCase.DeleteCartUseCase
import com.example.bio.domain.useCase.GetCartFullUseCase
import com.example.bio.domain.useCase.GetProfileDiscountUseCase
import com.example.bio.domain.useCase.PostCartEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val getCartFullUseCase: GetCartFullUseCase,
    private val postCartEventUseCase: PostCartEventUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getProfileDiscountUseCase: GetProfileDiscountUseCase
) : ViewModel() {

    private val _cartFull: MutableSharedFlow<CartFull> = MutableSharedFlow()
    val cartFull get() = _cartFull.asSharedFlow()

    private val _profileDiscount: MutableStateFlow<List<UserDiscount>> = MutableStateFlow(listOf())
    val profileDiscount get() = _profileDiscount.asStateFlow()

    private val _billMy: MutableStateFlow<List<FindOneOrderUserBill>> = MutableStateFlow(listOf())
    val billMy get() = _billMy.asStateFlow()

    fun getCartFull(token: String) {
        viewModelScope.launch {
            val res = getCartFullUseCase.execute(token)
            _cartFull.emit(res)
        }

        getProfileDiscount(token)
        getBillMy(token)
    }

    private fun getProfileDiscount(token: String) {
        viewModelScope.launch {
            val res = getProfileDiscountUseCase.execute(token)

            _profileDiscount.emit(res)
        }
    }

    fun postCart(token: String, postCart: PostCart) {
        viewModelScope.launch {
            postCartEventUseCase.execute(token, postCart)
        }

        getCartFull(token)
    }

    fun deleteCart(token: String, id: Int) {
        viewModelScope.launch {
            deleteCartUseCase.execute(token, id)
        }
    }

    fun getBillMy(token: String) {
        viewModelScope.launch {
            val res = retrofitProductCondition.getBillMy(token)
            _billMy.emit(res)
        }
    }

    fun createOrder(token: String, createCheckout: CreateCheckout) {
        viewModelScope.launch {
            retrofitProductCondition.createCheckout(token, createCheckout)
        }
    }

}

