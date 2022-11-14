package com.dondika.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.dondika.storyapp.data.StoryRemoteMediator
import com.dondika.storyapp.data.local.datastore.UserPreference
import com.dondika.storyapp.data.local.room.StoryDatabase
import com.dondika.storyapp.data.local.room.StoryEntity
import com.dondika.storyapp.data.remote.ApiService
import com.dondika.storyapp.data.remote.stories.StoryResponse
import com.dondika.storyapp.data.remote.stories.UploadResponse
import com.dondika.storyapp.data.remote.user.login.LoginRequest
import com.dondika.storyapp.data.remote.user.login.LoginResponse
import com.dondika.storyapp.data.remote.user.register.RegisterRequest
import com.dondika.storyapp.data.remote.user.register.RegisterResponse
import com.dondika.storyapp.utils.Result
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository constructor(
    private val apiService: ApiService,
    private val pref: UserPreference,
    private val storyDatabase: StoryDatabase
){

    fun loginUser(loginRequest: LoginRequest): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading())
        try {
            val response = apiService.login(loginRequest)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun registerUser(registerRequest: RegisterRequest): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading())
        try {
            val response = apiService.register(registerRequest)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    fun uploadStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?)
    : LiveData <Result<UploadResponse>> = liveData {
        emit(Result.Loading())
        try {
            val response = apiService.uploadStory("Bearer $token", file, description, lat, lon)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories(token:String): Flow<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(token, apiService, storyDatabase),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).flow
    }

    fun getAllStoriesWithLocation(token: String): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading())
        try {
            val response = apiService.getAllStories("Bearer $token", size = 100, location = 1)
            emit(Result.Success(response))
        } catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveUser(token: String) {
        pref.saveUser(token)
    }

    fun fetchUser() = pref.fetchUser()

    suspend fun deleteUser() = pref.deleteUser()


    companion object {
        private var INSTANCE: UserRepository? = null
        fun getInstance(apiService: ApiService, pref: UserPreference, storyDatabase: StoryDatabase): UserRepository {
            return INSTANCE ?: synchronized(this){
                UserRepository(apiService, pref, storyDatabase).also {
                    INSTANCE = it
                }
            }
        }
    }


}