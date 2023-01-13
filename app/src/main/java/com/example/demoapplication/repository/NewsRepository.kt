package com.example.demoapplication.repository

import com.example.demoapplication.api.UserLoginApi
import com.example.demoapplication.request.LoginRequest
import com.example.demoapplication.response.HeadLineResponse
import com.example.demoapplication.response.LoginResponse
import retrofit2.Response

class NewsRepository {
    suspend fun getNews(apiKey:String,country:String): Response<HeadLineResponse>?{
        return UserLoginApi.getApi()?.getHeadLines(apiKey,country)
    }
}