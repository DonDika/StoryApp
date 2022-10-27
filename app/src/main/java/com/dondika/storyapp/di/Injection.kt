package com.dondika.storyapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dondika.storyapp.data.local.UserPreference
import com.dondika.storyapp.data.remote.ApiConfig
import com.dondika.storyapp.repository.UserRepository

object Injection {

    fun provideRepository(dataStore: DataStore<Preferences>): UserRepository {
        val api = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(dataStore)
        return UserRepository.getInstance(api, pref)
    }

}