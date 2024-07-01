package com.example.bio.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.domain.entities.cart.CartMini
import com.example.bio.domain.entities.cart.PostCart
import com.example.bio.domain.entities.collectCharacters.CollectCharacter
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.entities.wishList.WishListCompareMini
import com.example.bio.domain.useCase.DeleteCartUseCase
import com.example.bio.domain.useCase.GetCollectCharactersUseCase
import com.example.bio.domain.useCase.GetMiniCartUseCase
import com.example.bio.domain.useCase.GetMiniCompareUseCase
import com.example.bio.domain.useCase.GetMiniWishListGetUseCase
import com.example.bio.domain.useCase.GetSearchResultsUseCase
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
class SearchViewModel @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
    private val getCollectCharactersUseCase: GetCollectCharactersUseCase,
    private val getMiniWishListGetUseCase: GetMiniWishListGetUseCase,
    private val getMiniCompareUseCase: GetMiniCompareUseCase,
    private val getMiniCartUseCase: GetMiniCartUseCase,
    private val postWishListEventUseCase: PostWishListEventUseCase,
    private val postCompareEventUseCase: PostCompareEventUseCase,
    private val postCartEventUseCase: PostCartEventUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
) : ViewModel() {

    private val _listProduct: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val listProduct get() = _listProduct.asStateFlow()

    private val _charactersCategory: MutableSharedFlow<CollectCharacter> = MutableSharedFlow()
    val characterCategory get() = _charactersCategory.asSharedFlow()

    private val _wishListMini: MutableSharedFlow<List<WishListCompareMini>> = MutableSharedFlow()
    val wishListMini get() = _wishListMini.asSharedFlow()

    private val _compareMini: MutableSharedFlow<List<WishListCompareMini>> = MutableSharedFlow()
    val compareMini get() = _compareMini.asSharedFlow()

    private val _cartMini: MutableSharedFlow<CartMini> = MutableSharedFlow()
    val cartMini get() = _cartMini.asSharedFlow()

    fun getSearchResults(token: String, message: String) {
        viewModelScope.launch {
            val res = getSearchResultsUseCase.execute(token, message)
            _listProduct.emit(res)
            getWishListMini(token)
            getCompareMini(token)
            getCartMini(token)
        }
    }

    fun getCharacters(token: String, catalog: String) {
        viewModelScope.launch {
            Log.d("Mylog", "Get characters open")
            val res = getCollectCharactersUseCase.execute(token, catalog)
            Log.d("Mylog", "Get characters close, res = ${res.characters.size}")
            _charactersCategory.emit(res)
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
