package com.dondika.storyapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dondika.storyapp.repository.UserRepository

class SplashViewModel(private val repository: UserRepository) : ViewModel() {

    fun fetchUser(): LiveData<String>{
        return repository.fetchUser().asLiveData()
    }

}