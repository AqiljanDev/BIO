package com.example.login.ui.main.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.Repository
import com.example.login.data.dataClass.LoginData
import com.example.login.ui.main.StateSealedClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
) : ViewModel() {
    private val repository: Repository = Repository()
    private val _stateLogin: MutableStateFlow<StateSealedClass> = MutableStateFlow(value = StateSealedClass.Loading)
    val stateLogin = _stateLogin.asStateFlow()

    fun login(loginData: LoginData) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _stateLogin.emit(StateSealedClass.Loading)
                repository.login(loginData)
            }.fold(
                onSuccess = {
                    Log.d("Mylog", "On Success = $it")
                    _stateLogin.emit(StateSealedClass.Success(it.accessToken.toString()))
                },
                onFailure = {
                    Log.d("Mylog", "On failure = $it")
                    _stateLogin.emit(StateSealedClass.Failed(it.message.toString()))
                }
            )
        }

    }

}