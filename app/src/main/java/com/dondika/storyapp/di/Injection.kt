package com.dondika.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dondika.storyapp.data.local.datastore.UserPreference
import com.dondika.storyapp.data.local.room.StoryDatabase
import com.dondika.storyapp.data.remote.ApiConfig
import com.dondika.storyapp.repository.UserRepository

object Injection {

    fun provideRepository(context: Context ,dataStore: DataStore<Preferences>): UserRepository {
        val api = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(dataStore)
        val database = StoryDatabase.getDatabase(context)
        return UserRepository.getInstance(api, pref, database)
    }

}