package com.dondika.storyapp.data.remote

import com.dondika.storyapp.data.remote.stories.StoryResponse
import com.dondika.storyapp.data.remote.stories.UploadResponse
import com.dondika.storyapp.data.remote.user.LoginRequest
import com.dondika.storyapp.data.remote.user.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>



    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String
    ): Response<StoryResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<UploadResponse>


}