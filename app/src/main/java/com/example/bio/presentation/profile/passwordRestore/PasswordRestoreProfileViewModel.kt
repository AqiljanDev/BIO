package com.example.bio.presentation.profile.passwordRestore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.data.api.retrofitPassRestore
import com.example.login.data.dataClass.PasswordCodeSendData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRestoreProfileViewModel @Inject constructor() : ViewModel() {
    private val _stateRestore: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val stateRestore get() = _stateRestore.asSharedFlow()


    fun restoreSendCode(data: PasswordCodeSendData) {
        viewModelScope.launch {
            try {
                val res = retrofitPassRestore.passwordCodeSend(data)
                _stateRestore.emit(true)
            } catch (e: Exception) {
                Log.d("Mylog", "${e.message}")
                _stateRestore.emit(false)
            }
        }
    }

}