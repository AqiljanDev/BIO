package com.example.login.ui.main.passwordRestore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.api.retrofitPassRestore
import com.example.login.data.dataClass.PasswordCodeSendData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class PasswordRestoreViewModel : ViewModel() {
    private val _stateRestore: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val stateRestore get() = _stateRestore.asSharedFlow()


    fun restoreSendCode(data: PasswordCodeSendData) {
        viewModelScope.launch {
            try {
                retrofitPassRestore.passwordCodeSend(data)
                _stateRestore.emit(true)
            } catch (e: Exception) {
                Log.d("Mylog", "${e.message}")
                _stateRestore.emit(false)
            }
        }
    }

}