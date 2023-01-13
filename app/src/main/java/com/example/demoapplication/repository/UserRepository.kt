package com.example.demoapplication.repository

import com.example.demoapplication.api.UserLoginApi
import com.example.demoapplication.networking.ApiClient
import com.example.demoapplication.request.LoginRequest
import com.example.demoapplication.response.LoginResponse
import retrofit2.Response
import retrofit2.create

class UserRepository {
    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>?{
        return UserLoginApi.getApi()?.loginUser(loginRequest)
    }
}