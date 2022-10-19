package com.dondika.storyapp.data.remote

import com.dondika.storyapp.data.remote.user.LoginRequest
import com.dondika.storyapp.data.remote.user.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

}