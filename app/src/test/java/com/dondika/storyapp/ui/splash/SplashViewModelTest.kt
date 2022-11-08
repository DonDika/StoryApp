package com.dondika.storyapp.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.dondika.storyapp.repository.UserRepository
import com.dondika.storyapp.utils.MainDispatcherRule
import org.junit.Assert.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var splashViewModel: SplashViewModel
    private val dummyToken = "token"

    @Before
    fun setup(){
        splashViewModel = SplashViewModel(userRepository)
    }

    @Test
    fun `when Fetch Token Successfully`() = runTest {
        val tokenExpected = MutableLiveData<String>()
        tokenExpected.value = dummyToken

        `when`(userRepository.fetchUser()).thenReturn(tokenExpected.asFlow())

        val tokenActual = splashViewModel.fetchUser()
        verify(userRepository).fetchUser()
        assertNotNull(tokenActual)
    }

}