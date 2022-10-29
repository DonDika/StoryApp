package com.dondika.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dondika.storyapp.data.StoryRemoteMediator
import com.dondika.storyapp.data.local.datastore.UserPreference
import com.dondika.storyapp.data.local.room.StoryDatabase
import com.dondika.storyapp.data.local.room.StoryEntity
import com.dondika.storyapp.data.remote.ApiService
import com.dondika.storyapp.data.remote.user.login.LoginRequest
import com.dondika.storyapp.data.remote.user.register.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreference,
    private val storyDatabase: StoryDatabase
){

    suspend fun login(loginRequest: LoginRequest) = apiService.login(loginRequest)

    suspend fun register(registerRequest: RegisterRequest) = apiService.register(registerRequest)

    suspend fun getAllStories(token: String) = apiService.getAllStories("Bearer $token")


    @OptIn(ExperimentalPagingApi::class)
    fun pagingGetAllStories(token:String): LiveData<PagingData<StoryEntity>>{
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(token, apiService, storyDatabase),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStories()
            }
        ).liveData
    }

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
        fun getInstance(apiService: ApiService, pref: UserPreference, storyDatabase: StoryDatabase): UserRepository {
            return INSTANCE ?: synchronized(this){
                UserRepository(apiService, pref, storyDatabase).also {
                    INSTANCE = it
                }
            }
        }
    }

}