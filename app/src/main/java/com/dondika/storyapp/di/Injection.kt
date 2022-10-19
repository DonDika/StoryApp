package com.dondika.storyapp.di

import com.dondika.storyapp.data.remote.ApiConfig
import com.dondika.storyapp.repository.UserRepository

object Injection {

    fun provideRepository(): UserRepository {
        val api = ApiConfig.getApiService()
        return UserRepository.getInstance(api)
    }

}