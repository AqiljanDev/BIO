package com.example.bio.presentation.left_menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bio.domain.entities.CategoriesFindAll
import com.example.bio.domain.useCase.GetCategoriesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesListViewModel @Inject constructor(
    private val getCategoriesListUseCase: GetCategoriesListUseCase
) : ViewModel() {

    private val _categoriesList: MutableSharedFlow<List<CategoriesFindAll>> = MutableSharedFlow()
    val categoryList get() = _categoriesList.asSharedFlow()


    fun getList(token: String) {
        viewModelScope.launch {
            val res = getCategoriesListUseCase.execute(token)
            Log.d("Mylog", "GET LIST ACTIVE = $res")
            _categoriesList.emit(res)
        }
    }
}