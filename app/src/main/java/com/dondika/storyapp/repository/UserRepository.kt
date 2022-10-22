package com.dondika.storyapp.repository

import com.dondika.storyapp.data.remote.ApiService
import com.dondika.storyapp.data.remote.user.LoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(private val apiService: ApiService){

    suspend fun login(loginRequest: LoginRequest) = apiService.login(loginRequest)


    suspend fun getAllStories(token: String) = apiService.getAllStories("Bearer $token")

    suspend fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody) =
        apiService.uploadImage(token, file, description)

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