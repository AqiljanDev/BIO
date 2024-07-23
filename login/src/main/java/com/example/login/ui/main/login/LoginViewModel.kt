package com.example.login.ui.main.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.Repository
import com.example.login.data.dataClass.LoginData
import com.example.login.data.dataClass.returnData.ApiError
import com.example.login.data.dataClass.returnData.AuthReturnClass
import com.example.login.ui.main.StateSealedClass
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.Response

class LoginViewModel(
) : ViewModel() {
    private val repository: Repository = Repository()
    private val _stateLogin: MutableStateFlow<StateSealedClass> =
        MutableStateFlow(value = StateSealedClass.Loading)
    val stateLogin = _stateLogin.asStateFlow()

    fun login(loginData: LoginData) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateLogin.emit(StateSealedClass.Loading)
            val result = repository.login(loginData)

            handleCheck(result)
        }

    }

    private suspend fun handleCheck(response: Response<AuthReturnClass>) {
        try {
            if (response.isSuccessful) {
                val authReturn = response.body()
                Log.d("MyLog", "Request successful: $authReturn")
                authReturn?.let {
                    authReturn.accessToken?.let {
                        _stateLogin.emit(StateSealedClass.Success(it))
                    }
                }
            } else {
                val errorBody = response.errorBody()?.string()
                if (errorBody != null) {
                    try {
                        // Попробуем разобрать как ApiError
                        val error = Gson().fromJson(errorBody, ApiError::class.java)

                        // Обрабатываем message в зависимости от его типа
                        when (error.message) {
                            is String -> {
                                Log.d("MyLog", "Error received: ${error.message}")
                                _stateLogin.emit(StateSealedClass.Failed(error.message))
                            }

                            is List<*> -> {
                                (error.message as List<*>).forEach { msg ->
                                    Log.d("MyLog", "Error received: $msg")
                                    _stateLogin.emit(StateSealedClass.Failed(msg as String))
                                }
                            }

                            else -> {
                                Log.d("MyLog", "Unknown error format: ${error.message}")
                            }
                        }
                    } catch (jsonException: JsonSyntaxException) {
                        // Если не удается разобрать как ApiError, печатаем исходное сообщение об ошибке
                        Log.d("MyLog", "Failed to parse JSON error: ${jsonException.message}")
                        Log.d("MyLog", "Raw error message: $errorBody")
                    }
                } else {
                    Log.d("MyLog", "Unknown error: ${response.message()}")
                }
            }
        } catch (e: Exception) {
            Log.d("MyLog", "Request failed: ${e.message}")
        }
    }

}
