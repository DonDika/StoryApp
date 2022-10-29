package com.dondika.storyapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dondika.storyapp.di.Injection
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.ui.home.HomeViewModel
import com.dondika.storyapp.ui.map.MapsViewModel
import com.dondika.storyapp.ui.splash.SplashViewModel
import com.dondika.storyapp.ui.upload.UploadStoryViewModel
import com.dondika.storyapp.ui.user.login.LoginViewModel
import com.dondika.storyapp.ui.user.register.RegisterViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_account")

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UploadStoryViewModel::class.java) -> {
                UploadStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        private var INSTANCE: UserViewModelFactory? = null
        fun getInstance(context: Context): UserViewModelFactory {
            return INSTANCE ?: synchronized(this){
                UserViewModelFactory(Injection.provideRepository(context, context.dataStore)).also {
                    INSTANCE = it
                }
            }
        }

    }

}