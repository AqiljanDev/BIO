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
import retrofit2.Response

class Repository {
    suspend fun login(loginData: LoginData): Response<AuthReturnClass> {
            return retrofitAuth.login(loginData)
    }

    suspend fun registration(registerData: RegisterData): Response<String> {

        return retrofitAuth.registration(registerData)
    }
}