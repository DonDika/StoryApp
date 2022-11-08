package com.dondika.storyapp.ui.user.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dondika.storyapp.data.remote.user.login.LoginRequest
import com.dondika.storyapp.data.remote.user.login.LoginResponse
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.DataDummy
import com.dondika.storyapp.utils.MainDispatcherRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.dondika.storyapp.utils.Result
import com.dondika.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginResponse = DataDummy.loginResponse()
    private val userLogin = LoginRequest("maguire@gmail.com", "123456")
    private val dummyToken = "token"

    @Before
    fun setup(){
        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `when Login Successfully`()  {
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Success(dummyLoginResponse)

        `when`(userRepository.loginUser(userLogin)).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.loginUser(userLogin).getOrAwaitValue()
        verify(userRepository).loginUser(userLogin)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(expectedResponse.value?.data, (actualResponse as Result.Success).data)
    }


    @Test
    fun `when Login Failed`() {
        val expectedResponse = MutableLiveData<Result<LoginResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(userRepository.loginUser(userLogin)).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.loginUser(userLogin).getOrAwaitValue()
        verify(userRepository).loginUser(userLogin)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }


    @Test
    fun `when Save Token Successfully`(): Unit = runTest {
        loginViewModel.saveUser(dummyToken)
        verify(userRepository).saveUser(dummyToken)
    }






}