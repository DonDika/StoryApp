package com.dondika.storyapp.ui.user.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dondika.storyapp.data.remote.user.login.LoginRequest
import com.dondika.storyapp.repository.UserRepository
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun loginUser(loginRequest: LoginRequest) = repository.loginUser(loginRequest)

    fun saveUser(token: String) {
        viewModelScope.launch {
            repository.saveUser(token)
        }
    }


}