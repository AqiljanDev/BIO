package com.example.bio.presentation.profile.orders.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.findOneOrder.FindOneOrder
import com.example.bio.domain.useCase.DeleteCartUseCase
import com.example.bio.domain.useCase.GetMiniCartUseCase
import com.example.bio.domain.useCase.GetOrdersFindOneUseCase
import com.example.bio.domain.useCase.PostCartEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryCardViewModel @Inject constructor(
    private val getOrdersFindOneUseCase: GetOrdersFindOneUseCase,
    private val getMiniCartUseCase: GetMiniCartUseCase,
    private val postCartEventUseCase: PostCartEventUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
) : ViewModel() {

    private val _orderFindOne: MutableSharedFlow<FindOneOrder> = MutableSharedFlow()
    val orderFindOne get() = _orderFindOne.asSharedFlow()

    private val _cartMini: MutableSharedFlow<CartMini> = MutableSharedFlow()
    val cartMini get() = _cartMini.asSharedFlow()


    fun getOrderFindOne(token: String, id: Int) {
        viewModelScope.launch {
            val res = getOrdersFindOneUseCase.execute(token, id)

            _orderFindOne.emit(res)
        }
        getCartMini(token)
    }

    private fun getCartMini(token: String) {
        viewModelScope.launch {
            val res = getMiniCartUseCase.execute(token)
            _cartMini.emit(res)
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