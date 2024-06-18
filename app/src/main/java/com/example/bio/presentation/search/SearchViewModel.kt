package com.example.bio.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.domain.entities.findOne.Product
import com.example.bio.domain.useCase.GetSearchResultsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultsUseCase
) : ViewModel() {
    private val _listProduct: MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val listProduct = _listProduct.asStateFlow()


    fun getSearchResults(token: String, message: String) {
        viewModelScope.launch {
            val res = getSearchResultsUseCase.execute(token, message)

            _listProduct.emit(res)
        }
    }

}