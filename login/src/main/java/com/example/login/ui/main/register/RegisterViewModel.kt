package com.example.login.ui.main.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.Repository
import com.example.login.data.dataClass.RegisterData
import com.example.login.data.dataClass.returnData.ApiError
import com.example.login.ui.main.StateSealedClass
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class RegisterViewModel(
) : ViewModel() {
    private val repository: Repository = Repository()
    private val _stateRegister: MutableStateFlow<StateSealedClass> =
        MutableStateFlow(StateSealedClass.Loading)
    val stateRegister = _stateRegister.asStateFlow()

    fun register(registerData: RegisterData) {
        viewModelScope.launch(Dispatchers.IO) {
            _stateRegister.emit(StateSealedClass.Loading)
            val res = repository.registration(registerData)

            handleCheck(res)
        }
    }


    private suspend fun handleCheck(response: Response<String>) {
        try {
            if (response.isSuccessful) {
                val authReturn = response.body()
                Log.d("MyLog", "Request successful: $authReturn")
                authReturn?.let {
                    _stateRegister.emit(StateSealedClass.Success(authReturn))
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
                                _stateRegister.emit(StateSealedClass.Failed(error.message))
                            }

                            is List<*> -> {
                                (error.message as List<*>).forEach { msg ->
                                    Log.d("MyLog", "Error received: $msg")
                                _stateRegister.emit(StateSealedClass.Failed(error.message.joinToString { "" }))
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