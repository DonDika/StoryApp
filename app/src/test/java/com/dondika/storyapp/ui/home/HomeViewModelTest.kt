package com.dondika.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.dondika.storyapp.data.local.room.StoryEntity
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var homeViewModel: HomeViewModel
    private val dummyPagingListStory = DataDummy.pagingListStory()
    private val dummyToken = "token"

    @Before
    fun setup(){
        homeViewModel = HomeViewModel(userRepository)
    }

    @Test
    fun `when Get All Stories With Paging Successfully`() = runTest {
        val data = PagingTest.snapshot(dummyPagingListStory)
        val expectedResponse: Flow<PagingData<StoryEntity>> = flow {
            emit(data)
        }

        `when`(userRepository.getAllStories(dummyToken)).thenReturn(
            expectedResponse
        )

        homeViewModel.getAllStories(dummyToken).observeForever {
            val asyncPagingDataDiffer = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = mainDispatcherRule.testDispatcher,
                workerDispatcher = mainDispatcherRule.testDispatcher
            )

            CoroutineScope(Dispatchers.IO).launch {
                asyncPagingDataDiffer.submitData(it)
            }

            advanceUntilIdle()

            verify(userRepository).getAllStories(dummyToken)
            assertNotNull(asyncPagingDataDiffer.snapshot())
        }
    }

    @Test
    fun `when Delete Token Successfully`(): Unit = runTest {
        homeViewModel.deleteUser()
        verify(userRepository).deleteUser()
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }




}