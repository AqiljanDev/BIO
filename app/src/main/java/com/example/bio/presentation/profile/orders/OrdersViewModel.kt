package com.example.bio.presentation.profile.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.domain.entities.myOrder.MyOrder
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.domain.useCase.GetOrdersFindMyUseCase
import com.example.bio.domain.useCase.GetProfileDiscountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getProfileDiscountUseCase: GetProfileDiscountUseCase,
    private val getOrdersFindMyUseCase: GetOrdersFindMyUseCase
) : ViewModel() {
    private val _profileDiscount: MutableStateFlow<List<UserDiscount>> = MutableStateFlow(listOf())
    val profileDiscount get() = _profileDiscount.asStateFlow()

    private val _ordersFindMy: MutableStateFlow<List<MyOrder>> = MutableStateFlow(listOf())
    val ordersFindMy get() = _ordersFindMy.asStateFlow()

    fun getAllRequest(token: String) {
        getProfileDiscount(token)
        getOrdersFindMy(token)
    }

    private fun getProfileDiscount(token: String) {
        viewModelScope.launch {
            val res = getProfileDiscountUseCase.execute(token)

            _profileDiscount.emit(res)
        }
    }

    private fun getOrdersFindMy(token: String) {
        viewModelScope.launch {
            val res = getOrdersFindMyUseCase.execute(token)

            _ordersFindMy.emit(res)
        }
    }
}