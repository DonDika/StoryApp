package com.dondika.storyapp.ui.upload

import android.util.Log
import androidx.lifecycle.*
import com.dondika.storyapp.data.remote.stories.UploadResponse
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.Result
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uploadResponse = MutableLiveData<Result<UploadResponse>>()
    val uploadResponse: LiveData<Result<UploadResponse>> = _uploadResponse

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody, lat: RequestBody?, lon: RequestBody?) = viewModelScope.launch {
        _uploadResponse.value = Result.Loading()
        try {
            val response = repository.uploadStory(token, file, description, lat, lon)
            //Log.e("CEK LOC", "$lat and $lon" )
            _uploadResponse.value = Result.Success(response.body()!!)
        } catch (e: Exception){
            _uploadResponse.value = Result.Error(e.message.toString())
        }
    }

    fun fetchUser(): LiveData<String>{
        return repository.fetchUser().asLiveData()
    }


}