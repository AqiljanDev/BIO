package com.example.login.ui.main.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.Repository
import com.example.login.data.dataClass.RegisterData
import com.example.login.ui.main.StateSealedClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: Repository
) : ViewModel() {
    private val _stateRegister: MutableStateFlow<StateSealedClass> = MutableStateFlow(StateSealedClass.Loading)
    val stateRegister = _stateRegister.asStateFlow()
    fun register(registerData: RegisterData) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {

                _stateRegister.emit(StateSealedClass.Loading)
                repository.registration(registerData)
            }.fold(
                onSuccess = {
                    _stateRegister.emit(StateSealedClass.Success(it.accessToken.toString()))
                },
                onFailure = {
                    _stateRegister.emit(StateSealedClass.Failed(it.message.toString()))
                }
            )
        }
    }
}