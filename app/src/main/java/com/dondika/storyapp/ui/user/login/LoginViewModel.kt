package com.dondika.storyapp.ui.user.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dondika.storyapp.data.remote.user.LoginRequest
import com.dondika.storyapp.data.remote.user.LoginResponse
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.Result
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<Result<LoginResponse>>()
    val loginResponse: LiveData<Result<LoginResponse>> = _loginResponse

    fun loginRequest(loginRequest: LoginRequest) = viewModelScope.launch {
        _loginResponse.value = Result.Loading()
        try {
            val response = repository.login(loginRequest)
            _loginResponse.value = Result.Success(response.body()!!)

        } catch (e: Exception){
            _loginResponse.value = Result.Error(e.message.toString())
            //Log.e("CEK", e.message.toString(), )
        }
    }


}