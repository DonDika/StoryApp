package com.dondika.storyapp.ui.upload

import androidx.lifecycle.*
import com.dondika.storyapp.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(private val repository: UserRepository) : ViewModel() {

    fun uploadStory(token: String, file: MultipartBody.Part,
                    description: RequestBody, lat: RequestBody?, lon: RequestBody?) =
        repository.uploadStory(token, file, description, lat, lon)


    fun fetchUser(): LiveData<String>{
        return repository.fetchUser().asLiveData()
    }


}