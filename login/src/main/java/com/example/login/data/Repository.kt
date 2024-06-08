package com.example.login.data

import android.util.Log
import com.example.login.data.api.retrofitAuth
import com.example.login.data.api.retrofitPassRestore
import com.example.login.data.dataClass.LoginData
import com.example.login.data.dataClass.PasswordChangeData
import com.example.login.data.dataClass.PasswordCodeCheckData
import com.example.login.data.dataClass.PasswordCodeSendData
import com.example.login.data.dataClass.RegisterData
import com.example.login.data.dataClass.returnData.AuthReturnClass

class Repository {
    suspend fun login(loginData: LoginData): AuthReturnClass {
        try {
            return retrofitAuth.login(loginData)
        } catch (ex: Exception) {
            throw Exception("Exception = ${ex.message}")
        }

    }

    suspend fun registration(registerData: RegisterData): AuthReturnClass {

            val retro = retrofitAuth.registration(registerData)
            Log.d(
                "MyLog", "Message filed = ${retro.message}, Error = ${retro.error}, " +
                        "StatusCode = ${retro.statusCode}, Token = ${retro.accessToken}"
            )

            return retro
    }

    suspend fun passwordCodeSend(passwordCodeSendData: PasswordCodeSendData): Boolean {
        return retrofitPassRestore.passwordCodeSend(passwordCodeSendData)
    }

    suspend fun passwordCodeCheck(passwordCodeCheckData: PasswordCodeCheckData): Boolean {
        return retrofitPassRestore.passwordCodeCheck(passwordCodeCheckData)
    }

    suspend fun passwordChange(passwordChangeData: PasswordChangeData): Boolean {
        return retrofitPassRestore.passwordChange(passwordChangeData)
    }
}