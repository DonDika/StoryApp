package com.dondika.storyapp.ui.user.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dondika.storyapp.data.remote.user.register.RegisterRequest
import com.dondika.storyapp.data.remote.user.register.RegisterResponse
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.Result
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private var _registerResponse = MutableLiveData<Result<RegisterResponse>>()
    val registerResponse: LiveData<Result<RegisterResponse>> = _registerResponse

    fun register(registerRequest: RegisterRequest) = viewModelScope.launch {
        _registerResponse.value = Result.Loading()
        try {
            val response = repository.register(registerRequest)
            _registerResponse.value = Result.Success(response.body()!!)
        } catch (e: Exception){
            _registerResponse.value = Result.Error(e.message)
        }


    }

}