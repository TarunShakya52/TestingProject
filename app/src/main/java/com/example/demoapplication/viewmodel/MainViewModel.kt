package com.example.demoapplication.viewmodel

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.demoapplication.repository.UserRepository
import com.example.demoapplication.request.LoginRequest
import com.example.demoapplication.response.BaseResponse
import com.example.demoapplication.response.LoginResponse
import kotlinx.coroutines.launch


class MainViewModel(application: Application) :AndroidViewModel(application)
{
    val userRepo:UserRepository = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()

    fun loginUser(email:String,password:String,validate:Validate){

        if (email.isEmpty()){
            validate.invalidEmail()
        }

        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(email,password)
                val response = userRepo.loginUser(loginRequest)
                if (response?.code() == 200) {
                    loginResult.value = BaseResponse.Success(response.body())
                } else {
                    loginResult.value = BaseResponse.Error(response?.message())
                }
            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            } }
        }

    interface Validate{
        fun invalidEmail()
        fun invalidPassword()
    }
}