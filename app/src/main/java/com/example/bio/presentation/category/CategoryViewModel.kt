package com.example.bio.presentation.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.domain.entities.findOne.Catalog
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.useCase.GetCatalogUseCase
import com.example.bio.domain.useCase.GetCollectCharactersUseCase
import com.example.bio.presentation.category.data.State
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
    private val getCollectCharactersUseCase: GetCollectCharactersUseCase
) : ViewModel() {

    private val _categoryProduct: MutableSharedFlow<Catalog> = MutableSharedFlow()
    val categoryProduct get() = _categoryProduct.asSharedFlow()

    private val _countPages: MutableStateFlow<Int> = MutableStateFlow(1)
    val countPages get() = _countPages.asStateFlow()

    private val _state = MutableStateFlow<State>(State.Loading)
    val state get() = _state.asStateFlow()

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
    }

    fun getPageButton(token: String, category: String) {
        viewModelScope.launch {
            val valueCount = getCollectCharactersUseCase
                .execute(token, category)
                .pages

            _countPages.emit(valueCount)
        }
    }

}