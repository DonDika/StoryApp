package com.dondika.storyapp.ui.map


import androidx.lifecycle.ViewModel
import com.dondika.storyapp.repository.UserRepository

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    fun getAllStoriesWithLocation(token: String) = repository.getAllStoriesWithLocation(token)

}