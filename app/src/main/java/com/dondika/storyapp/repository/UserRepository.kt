package com.dondika.storyapp.repository

import com.dondika.storyapp.data.remote.ApiService
import com.dondika.storyapp.data.remote.user.LoginRequest

class UserRepository private constructor(private val apiService: ApiService){

    suspend fun login(loginRequest: LoginRequest) = apiService.login(loginRequest)


    suspend fun getAllStories(token: String) = apiService.getAllStories("Bearer $token")


    companion object {
        private var INSTANCE: UserRepository? = null
        fun getInstance(apiService: ApiService): UserRepository {
            return INSTANCE ?: synchronized(this){
                UserRepository(apiService).also {
                    INSTANCE = it
                }
            }
        }
    }



}