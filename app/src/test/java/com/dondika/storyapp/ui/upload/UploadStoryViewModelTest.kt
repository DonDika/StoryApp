package com.dondika.storyapp.ui.upload

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.dondika.storyapp.data.remote.stories.UploadResponse
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.DataDummy
import com.dondika.storyapp.utils.MainDispatcherRule
import org.junit.Assert.*
import com.dondika.storyapp.utils.Result
import com.dondika.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UploadStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var uploadStoryViewModel: UploadStoryViewModel

    private val dummyToken = "token"
    private val dummyUploadResponse = DataDummy.storyUploadResponse()
    private val dummyMultipart = DataDummy.multipartFile()
    private val dummyDescription = DataDummy.dummyDescription()

    @Before
    fun setup(){
        uploadStoryViewModel = UploadStoryViewModel(userRepository)
    }

    @Test
    fun `when Upload Story Successfully`(){
        val expectedResponse = MutableLiveData<Result<UploadResponse>>()
        expectedResponse.value = Result.Success(dummyUploadResponse)

        `when`(userRepository.uploadStory(dummyToken,dummyMultipart,dummyDescription,null,null)).thenReturn(expectedResponse)

        val actualResponse = uploadStoryViewModel.uploadStory(dummyToken,dummyMultipart,dummyDescription,null,null).getOrAwaitValue()
        verify(userRepository).uploadStory(dummyToken,dummyMultipart,dummyDescription,null,null)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(expectedResponse.value?.data, (actualResponse as Result.Success).data)
    }

    @Test
    fun `when Upload Story Failed`(){
        val expectedResponse = MutableLiveData<Result<UploadResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(userRepository.uploadStory(dummyToken,dummyMultipart,dummyDescription,null,null)).thenReturn(expectedResponse)

        val actualResponse = uploadStoryViewModel.uploadStory(dummyToken,dummyMultipart,dummyDescription,null,null).getOrAwaitValue()
        verify(userRepository).uploadStory(dummyToken,dummyMultipart,dummyDescription,null,null)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }

    @Test
    fun `when Fetch Token Successfully`() = runTest {
        val tokenExpected = MutableLiveData<String>()
        tokenExpected.value = dummyToken

        `when`(userRepository.fetchUser()).thenReturn(tokenExpected.asFlow())

        val tokenActual = uploadStoryViewModel.fetchUser()
        verify(userRepository).fetchUser()
        assertNotNull(tokenActual)
    }


}