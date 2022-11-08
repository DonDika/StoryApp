package com.dondika.storyapp.utils

import com.dondika.storyapp.data.local.room.StoryEntity
import com.dondika.storyapp.data.remote.stories.ListStoryItem
import com.dondika.storyapp.data.remote.stories.StoryResponse
import com.dondika.storyapp.data.remote.stories.UploadResponse
import com.dondika.storyapp.data.remote.user.login.LoginResponse
import com.dondika.storyapp.data.remote.user.register.RegisterResponse
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {

    fun loginResponse(): LoginResponse {
        val json = """
            {
              "error": false,
              "message": "success",
              "loginResult": {
                "userId": "user-U_t1OvDmEJW2eHMM",
                "name": "MaguireTamvan",
                "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVVfdDFPdkRtRUpXMmVITU0iLCJpYXQiOjE2Njc2NTQxODF9.brBLbWglJjSuOCzijZUwRSfGIbA3UGHwZ03jeI9kSm0"
              }
            }
        """.trimIndent()
        return Gson().fromJson(json, LoginResponse::class.java)
    }

    fun registerResponse(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "success"
        )
    }

    fun multipartFile(): MultipartBody.Part {
        val dummyText = "text file"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun dummyDescription(): RequestBody {
        val dummyText = "text description"
        return dummyText.toRequestBody()
    }

    fun storyUploadResponse(): UploadResponse {
        return UploadResponse(
            error = false,
            message = "success"
        )
    }

    fun storiesResponse(): StoryResponse{
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<ListStoryItem>()

        for (i in 0..10) {
            val story = ListStoryItem(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "$i",
                description = "Lorem Ipsum",
                lon = -16.002,
                lat = -10.212
            )
            listStory.add(story)
        }

        return StoryResponse(error, message, listStory)
    }

    fun pagingListStory(): List<StoryEntity> {
        val items = arrayListOf<StoryEntity>()
        for (i in 0..10){
            val story = StoryEntity(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "User ke-$i",
                description = "Lorem Ipsum",
                lat = -16.002,
                lng = -10.212
            )
            items.add(story)
        }
        return items
    }


}