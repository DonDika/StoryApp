package com.dondika.storyapp.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dondika.storyapp.data.remote.stories.StoryResponse
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.DataDummy
import com.dondika.storyapp.utils.MainDispatcherRule
import com.dondika.storyapp.utils.Result
import com.dondika.storyapp.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyStoriesResponse = DataDummy.storiesResponse()
    private val dummyToken = "token"

    @Before
    fun setup(){
        mapsViewModel = MapsViewModel(userRepository)
    }

    @Test
    fun `when Get Story With Location Successfully`()  {
        val expectedResponse = MutableLiveData<Result<StoryResponse>>()
        expectedResponse.value = Result.Success(dummyStoriesResponse)

        `when`(userRepository.getAllStoriesWithLocation(dummyToken)).thenReturn(expectedResponse)

        val actualResponse = mapsViewModel.getAllStoriesWithLocation(dummyToken).getOrAwaitValue()
        verify(userRepository).getAllStoriesWithLocation(dummyToken)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(expectedResponse.value?.data, (actualResponse as Result.Success).data)
    }

    @Test
    fun `when Get Story With Location Failed`() {
        val expectedResponse = MutableLiveData<Result<StoryResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(userRepository.getAllStoriesWithLocation(dummyToken)).thenReturn(expectedResponse)

        val actualResponse = mapsViewModel.getAllStoriesWithLocation(dummyToken).getOrAwaitValue()
        verify(userRepository).getAllStoriesWithLocation(dummyToken)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }


}