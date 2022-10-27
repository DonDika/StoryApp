package com.dondika.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dondika.storyapp.data.remote.stories.StoryResponse
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel()  {

    private val _storyResponse = MutableLiveData<Result<StoryResponse>>()
    val storyResponse: LiveData<Result<StoryResponse>> = _storyResponse

    fun getAllStories(token: String) = viewModelScope.launch {
        _storyResponse.value = Result.Loading()
        try {
            val response = repository.getAllStories(token)
            _storyResponse.value = Result.Success(response.body()!!)
        } catch (e: Exception) {
            _storyResponse.value = Result.Error(e.message)
        }
    }

    fun deleteUser() = viewModelScope.launch {
        repository.deleteUser()
    }

}