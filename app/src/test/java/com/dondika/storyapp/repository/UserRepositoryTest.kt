package com.dondika.storyapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ListUpdateCallback
import com.dondika.storyapp.data.local.datastore.UserPreference
import com.dondika.storyapp.data.local.room.StoryDatabase
import com.dondika.storyapp.data.remote.ApiService
import com.dondika.storyapp.data.remote.stories.StoryResponse
import com.dondika.storyapp.data.remote.stories.UploadResponse
import com.dondika.storyapp.data.remote.user.login.LoginRequest
import com.dondika.storyapp.data.remote.user.login.LoginResponse
import com.dondika.storyapp.data.remote.user.register.RegisterRequest
import com.dondika.storyapp.data.remote.user.register.RegisterResponse
import com.dondika.storyapp.ui.home.StoryAdapter
import com.dondika.storyapp.utils.DataDummy
import com.dondika.storyapp.utils.MainDispatcherRule
import com.dondika.storyapp.utils.PagingTest
import org.junit.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var apiService: ApiService
    @Mock
    private lateinit var userPreference: UserPreference
    @Mock
    private lateinit var storyDatabase: StoryDatabase
    //@Mock
    private lateinit var userRepositoryMock: UserRepository
    private lateinit var userRepository: UserRepository

    private val dummyLoginResponse = DataDummy.loginResponse()
    private val dummyUserLogin = LoginRequest("maguire@gmail.com", "123456")
    private val dummyRegisterResponse = DataDummy.registerResponse()
    private val dummyUserRegister = RegisterRequest("ikhsan tamvan","ikhsan@gmail.com","123456")
    private val dummyUploadResponse = DataDummy.storyUploadResponse()
    private val dummyMultipart = DataDummy.multipartFile()
    private val dummyDescription = DataDummy.dummyDescription()
    private val dummyStoriesResponse = DataDummy.storiesResponse()
    private val dummyPagingListStory = DataDummy.pagingListStory()
    private val dummyToken = "token"

    @Before
    fun setup(){
        userRepository = UserRepository(apiService, userPreference, storyDatabase)
    }

    @Test
    fun `when Login Successfully`() = runTest {
        val item = MutableLiveData<LoginResponse>()
        item.value = dummyLoginResponse
        val data = userRepository.loginUser(dummyUserLogin)
        assertNotNull(data)
    }

    @Test
    fun `when Register Successfully`() = runTest {
        val item = MutableLiveData<RegisterResponse>()
        item.value = dummyRegisterResponse
        val data = userRepository.registerUser(dummyUserRegister)
        assertNotNull(data)
    }

    @Test
    fun `when Upload Story Successfully`() = runTest{
        val item = MutableLiveData<UploadResponse>()
        item.value = dummyUploadResponse
        val data = userRepository.uploadStory(dummyToken, dummyMultipart, dummyDescription, null, null)
        assertNotNull(data)
    }

    @Test
    fun `when Get All Stories With Paging Successfully`() = runTest {
        val storyEntityPagingData = PagingTest.snapshot(dummyPagingListStory)
        val expectedResult = flowOf(storyEntityPagingData)

        `when`(userRepositoryMock.getAllStories(dummyToken)).thenReturn(expectedResult)

        userRepositoryMock.getAllStories(dummyToken).collect {
            val asyncPagingDataDiffer = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = mainDispatcherRule.testDispatcher,
                workerDispatcher = mainDispatcherRule.testDispatcher
            )
            asyncPagingDataDiffer.submitData(it)
            assertNotNull(asyncPagingDataDiffer.snapshot())
            assertEquals(dummyPagingListStory, asyncPagingDataDiffer.snapshot().size)
        }
    }

    @Test
    fun `when Get Story With Location Successfully`() = runTest{
        val item = MutableLiveData<StoryResponse>()
        item.value = dummyStoriesResponse
        val data = userRepository.getAllStoriesWithLocation(dummyToken)
        assertNotNull(data)
    }

    @Test
    fun `when Save Token Successfully`() = runTest {
        userRepository.saveUser(dummyToken)
        verify(userPreference).saveUser(dummyToken)
    }

    @Test
    fun `when Fetch Token Successfully`() = runTest {
        val tokenExpected = MutableLiveData<String>()
        tokenExpected.value = dummyToken
        `when`(userRepository.fetchUser()).thenReturn(tokenExpected.asFlow())
        val tokenActual = userRepository.fetchUser()
        assertNotNull(tokenActual)
        verify(userPreference).fetchUser()
    }

    @Test
    fun `when Delete Token Successfully`() = runTest {
        userRepository.deleteUser()
        verify(userPreference).deleteUser()
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }


}