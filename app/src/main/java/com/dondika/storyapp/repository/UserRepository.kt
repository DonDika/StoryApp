package com.dondika.storyapp.repository

import com.dondika.storyapp.data.local.UserPreference
import com.dondika.storyapp.data.remote.ApiService
import com.dondika.storyapp.data.remote.user.login.LoginRequest
import com.dondika.storyapp.data.remote.user.register.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreference){

    suspend fun login(loginRequest: LoginRequest) = apiService.login(loginRequest)

    suspend fun register(registerRequest: RegisterRequest) = apiService.register(registerRequest)


    suspend fun getAllStories(token: String) = apiService.getAllStories("Bearer $token")


    suspend fun getAllStoriesWithLocation(token: String) =
        apiService.getAllStories("Bearer $token", size = 20, location = 1)


    suspend fun uploadStory(token: String, file: MultipartBody.Part,
                            description: RequestBody, lat: RequestBody?, lon: RequestBody?) =
        apiService.uploadImage("Bearer $token", file, description, lat, lon)


    suspend fun saveUser(token: String) = pref.saveUser(token)

    fun fetchUser() = pref.fetchUser()

    suspend fun deleteUser() = pref.deleteUser()


    companion object {
        private var INSTANCE: UserRepository? = null
        fun getInstance(apiService: ApiService, pref: UserPreference): UserRepository {
            return INSTANCE ?: synchronized(this){
                UserRepository(apiService, pref).also {
                    INSTANCE = it
                }
            }
        }
    }

}