package com.example.bio.presentation.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.data.api.retrofitProductCondition
import com.example.bio.data.dto.CreateCheckout
import com.example.bio.data.dto.OrderDetails
import com.example.bio.domain.entities.cart.CartFull
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.useCase.DeleteCartUseCase
import com.example.bio.domain.useCase.GetCartFullUseCase
import com.example.bio.domain.useCase.PostCartEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketViewModel @Inject constructor(
    private val getCartFullUseCase: GetCartFullUseCase,
    private val postCartEventUseCase: PostCartEventUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
) : ViewModel() {

    private val _cartFull: MutableSharedFlow<CartFull> = MutableSharedFlow()
    val cartFull get() = _cartFull.asSharedFlow()

    private val _cartRes: MutableSharedFlow<OrderDetails> = MutableSharedFlow()
    val cartRes get() = _cartRes.asSharedFlow()


    fun getCartFull(token: String) {
        viewModelScope.launch {
            val res = getCartFullUseCase.execute(token)
            _cartFull.emit(res)
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

    fun createOrder(token: String, createCheckout: CreateCheckout) {
        viewModelScope.launch {
            retrofitProductCondition.createCheckout(token, createCheckout)
        }
    }

}

