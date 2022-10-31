package com.dondika.storyapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dondika.storyapp.data.local.room.StoryEntity
import com.dondika.storyapp.data.remote.stories.StoryResponse
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.Result
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel()  {

    fun getAllStories(token: String): LiveData<PagingData<StoryEntity>> =
        repository.getAllStories(token).cachedIn(viewModelScope)

    fun deleteUser() = viewModelScope.launch {
        repository.deleteUser()
    }

}