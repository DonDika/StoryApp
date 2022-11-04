package com.dondika.storyapp.ui.user.register

import androidx.lifecycle.ViewModel
import com.dondika.storyapp.data.remote.user.register.RegisterRequest
import com.dondika.storyapp.repository.UserRepository


class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    fun registerUser(registerRequest: RegisterRequest) =
        repository.registerUser(registerRequest)

}