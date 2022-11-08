package com.dondika.storyapp.ui.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dondika.storyapp.data.local.room.StoryEntity
import com.dondika.storyapp.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel()  {

    fun getAllStories(token: String): LiveData<PagingData<StoryEntity>> =
        repository.getAllStories(token).cachedIn(viewModelScope).asLiveData()

    fun deleteUser() = viewModelScope.launch {
        repository.deleteUser()
    }

}