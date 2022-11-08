package com.dondika.storyapp.utils

import androidx.datastore.core.DataStore
import androidx.room.Room
import com.dondika.storyapp.data.local.datastore.UserPreference
import com.dondika.storyapp.data.local.room.StoryDatabase
import com.dondika.storyapp.data.remote.ApiService
import com.dondika.storyapp.repository.UserRepository
import org.robolectric.RuntimeEnvironment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Module {

    fun getDatabase(): StoryDatabase {
        return Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            StoryDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    /*fun getPreference(): UserPreference {
        return DataStore
    }*/

    fun getApiService(): ApiService {
        return Retrofit
            .Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }

    /*fun getRepository(): UserRepository {
        return UserRepository(
            getApiService(),
            //getApiService(),
            getDatabase()

        )
    }*/


}