package com.example.demoapplication.api

import com.example.demoapplication.networking.ApiClient
import com.example.demoapplication.request.LoginRequest
import com.example.demoapplication.response.HeadLineResponse
import com.example.demoapplication.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// it should be named as api interface
interface UserLoginApi {

    @POST("/api/authaccount/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>
    @GET("top-headlines")
    suspend fun getHeadLines(@Query("apiKey")apiKey:String, @Query("country")country:String):Response<HeadLineResponse>

    companion object {
        fun getApi(): UserLoginApi? {
            return ApiClient.client?.create(UserLoginApi::class.java)
        }

    }

}