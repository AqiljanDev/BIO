package com.example.bio.presentation.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.bio.domain.useCase.DeleteCartUseCase
import com.example.bio.domain.useCase.GetCatalogUseCase
import com.example.bio.domain.useCase.GetCollectCharactersUseCase
import com.example.bio.domain.useCase.GetMiniCartUseCase
import com.example.bio.domain.useCase.GetMiniCompareUseCase
import com.example.bio.domain.useCase.GetMiniWishListGetUseCase
import com.example.bio.domain.useCase.PostCartEventUseCase
import com.example.bio.domain.useCase.PostCompareEventUseCase
import com.example.bio.domain.useCase.PostWishListEventUseCase
import com.example.bio.presentation.data.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val getCollectCharactersUseCase: GetCollectCharactersUseCase,
    private val getMiniWishListGetUseCase: GetMiniWishListGetUseCase,
    private val getMiniCompareUseCase: GetMiniCompareUseCase,
    private val getMiniCartUseCase: GetMiniCartUseCase,
    private val postWishListEventUseCase: PostWishListEventUseCase,
    private val postCompareEventUseCase: PostCompareEventUseCase,
    private val postCartEventUseCase: PostCartEventUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
) : ViewModel() {

    private val _saveCategory: MutableStateFlow<String> = MutableStateFlow("index")
    val saveCategory get() = _saveCategory.asStateFlow()

    private val _savePages: MutableStateFlow<Int> = MutableStateFlow(1)
    val savePages get() = _savePages.asStateFlow()

    private val _categoryProduct: MutableSharedFlow<Catalog> = MutableSharedFlow()
    val categoryProduct get() = _categoryProduct.asSharedFlow()

    private val _countPages: MutableStateFlow<Int> = MutableStateFlow(1)
    val countPages get() = _countPages.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Loading)
    val state get() = _state.asStateFlow()

    private val _wishListMini: MutableSharedFlow<List<WishListCompareMini>> = MutableSharedFlow()
    val wishListMini get() = _wishListMini.asSharedFlow()

    private val _compareMini: MutableSharedFlow<List<WishListCompareMini>> = MutableSharedFlow()
    val compareMini get() = _compareMini.asSharedFlow()

    private val _cartMini: MutableSharedFlow<CartMini> = MutableSharedFlow()
    val cartMini get() = _cartMini.asSharedFlow()

    fun getCategoryProduct(token: String, category: String, page: Int) {
        viewModelScope.launch {
            _state.emit(State.Loading)

            try {
                val res = getCatalogUseCase.execute(token, category, page)
                _categoryProduct.emit(res)

                _state.emit(State.Success)
            } catch (ex: Exception) {

                _state.emit(State.Failed(ex.message.toString()))
                Log.d("Mylog", "Exception: $ex ==== message: ${ex.message}")
            }
        }

        getPageButton(token, category)
        getWishListMini(token)
        getCompareMini(token)
        getCartMini(token)
    }

    private fun getPageButton(token: String, category: String) {
        viewModelScope.launch {
            val valueCount = getCollectCharactersUseCase
                .execute(token, category)
                .pages

            _countPages.emit(valueCount)
        }
    }

    private fun getWishListMini(token: String) {
        viewModelScope.launch {
            val res = getMiniWishListGetUseCase
                .execute(token)

            _wishListMini.emit(res)
        }
    }

    private fun getCompareMini(token: String) {
        viewModelScope.launch {
            val res = getMiniCompareUseCase
                .execute(token)

            _compareMini.emit(res)
        }
    }

    private fun getCartMini(token: String) {
        viewModelScope.launch {
            val res = getMiniCartUseCase
                .execute(token)

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

    fun savesCategory(category: String) {
        viewModelScope.launch {
            _saveCategory.emit(category)
        }
    }

    fun savesPage(currentPage: Int) {
        viewModelScope.launch {
            _savePages.emit(currentPage)
        }
    }

}