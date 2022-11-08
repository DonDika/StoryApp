package com.dondika.storyapp.ui.user.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dondika.storyapp.data.remote.user.register.RegisterRequest
import com.dondika.storyapp.data.remote.user.register.RegisterResponse
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.DataDummy
import com.dondika.storyapp.utils.MainDispatcherRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import com.dondika.storyapp.utils.Result
import com.dondika.storyapp.utils.getOrAwaitValue


@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegisterResponse = DataDummy.registerResponse()
    private val userRegister = RegisterRequest("ikhsan tamvan","ikhsan@gmail.com","123456")

    @Before
    fun setup(){
        registerViewModel = RegisterViewModel(userRepository)
    }

    @Test
    fun `when Register Successfully`(){
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Success(dummyRegisterResponse)

        `when`(userRepository.registerUser(userRegister)).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.registerUser(userRegister).getOrAwaitValue()
        verify(userRepository).registerUser(userRegister)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Success)
        assertEquals(expectedResponse.value?.data, (actualResponse as Result.Success).data)
    }

    @Test
    fun `when Register Failed`() {
        val expectedResponse = MutableLiveData<Result<RegisterResponse>>()
        expectedResponse.value = Result.Error("Error")

        `when`(userRepository.registerUser(userRegister)).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.registerUser(userRegister).getOrAwaitValue()
        verify(userRepository).registerUser(userRegister)
        assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }


}