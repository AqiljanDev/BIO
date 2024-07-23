package com.example.bio.presentation.compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.collectCharacters.Brand
import com.example.bio.domain.entities.compare.CompareFull
import com.example.bio.domain.entities.userDiscount.UserDiscount
import com.example.bio.domain.useCase.DeleteCartUseCase
import com.example.bio.domain.useCase.GetCollectCharactersUseCase
import com.example.bio.domain.useCase.GetCompareFullUseCase
import com.example.bio.domain.useCase.GetMiniCartUseCase
import com.example.bio.domain.useCase.GetProfileDiscountUseCase
import com.example.bio.domain.useCase.PostCartEventUseCase
import com.example.bio.domain.useCase.PostCompareEventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(
    private val getCompareFullUseCase: GetCompareFullUseCase,
    private val getCollectCharactersUseCase: GetCollectCharactersUseCase,
    private val getMiniCartUseCase: GetMiniCartUseCase,
    private val postCartEventUseCase: PostCartEventUseCase,
    private val postCompareEventUseCase: PostCompareEventUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val getProfileDiscountUseCase: GetProfileDiscountUseCase
): ViewModel() {
    private val _compareList: MutableSharedFlow<CompareFull> = MutableSharedFlow()
    val compareList get() = _compareList.asSharedFlow()

    private val _collectCharacter: MutableSharedFlow<List<Brand>> = MutableSharedFlow()
    val collectCharacter get() = _collectCharacter.asSharedFlow()

    private val _getCartMini: MutableSharedFlow<CartMini> = MutableSharedFlow()
    val getCartMini get() = _getCartMini.asSharedFlow()

    private val _profileDiscount: MutableStateFlow<List<UserDiscount>> = MutableStateFlow(listOf())
    val profileDiscount get() = _profileDiscount.asStateFlow()

    fun getCompares(token: String) {
        viewModelScope.launch {
            val res = getCompareFullUseCase.execute(token)
            _compareList.emit(res)
        }
        getCartMini(token)
        getProfileDiscount(token)
    }

    private fun getProfileDiscount(token: String) {
        viewModelScope.launch {
            val res = getProfileDiscountUseCase.execute(token)

            _profileDiscount.emit(res)
        }
    }

    fun getCollectCharacter(token: String, slug: String) {
        viewModelScope.launch {
            val res = getCollectCharactersUseCase.execute(token, slug)

            _collectCharacter.emit(res.characters)
        }
    }

    private fun getCartMini(token: String) {
        viewModelScope.launch {
            val res = getMiniCartUseCase.execute(token)
            _getCartMini.emit(res)
        }
    }

    fun postCompare(token: String, prodId: String) {
        viewModelScope.launch {
            postCompareEventUseCase.execute(token, prodId)
        }
        getCompares(token)
    }

    fun postCart(token: String, postCart: PostCart) {
        viewModelScope.launch {
            postCartEventUseCase.execute(token, postCart)
        }
        getCompares(token)
    }

    fun deleteCart(token: String, id: Int) {
        viewModelScope.launch {
            deleteCartUseCase.execute(token, id)
            // После удаления элемента обновите сравнение и корзину
            val res = getCompareFullUseCase.execute(token)
            _compareList.emit(res) // Обновите данные сравнения с новыми характеристиками
            getCartMini(token) // Обновите корзину
        }
    }
}

