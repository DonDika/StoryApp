package com.dondika.storyapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import com.dondika.storyapp.data.local.datastore.IUserPreference
import com.dondika.storyapp.data.local.datastore.UserPreference
import com.dondika.storyapp.data.local.room.StoryDao
import com.dondika.storyapp.data.local.room.StoryDatabase
import com.dondika.storyapp.data.remote.ApiService
import com.dondika.storyapp.ui.home.StoryAdapter
import com.dondika.storyapp.utils.MainDispatcherRule
import com.dondika.storyapp.utils.PagingTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class PagingTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    private lateinit var apiService: ApiService
    private lateinit var userPreference: IUserPreference
    private lateinit var storyDatabase: StoryDatabase




    /*@Test
    fun `when Get All Stories With Paging Successfully`() = runTest {
        val data = PagingTest.snapshot(dummyPagingListStory)
        val expectedResult = flowOf(data)

        Mockito.`when`(userRepositoryMock.getAllStories(dummyToken)).thenReturn(expectedResult)

        userRepositoryMock.getAllStories(dummyToken).collect {
            val asyncPagingDataDiffer = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = mainDispatcherRule.testDispatcher,
                workerDispatcher = mainDispatcherRule.testDispatcher
            )
            asyncPagingDataDiffer.submitData(it)

            Assert.assertNotNull(asyncPagingDataDiffer.snapshot())
            Assert.assertEquals(dummyStoriesResponse.listStory.size, asyncPagingDataDiffer.snapshot().size)
        }
    }*/


}