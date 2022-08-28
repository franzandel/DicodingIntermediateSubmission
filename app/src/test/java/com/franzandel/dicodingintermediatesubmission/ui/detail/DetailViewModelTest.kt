package com.franzandel.dicodingintermediatesubmission.ui.detail

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThread
import com.franzandel.dicodingintermediatesubmission.core.coroutine.CoroutineThreadImpl
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import com.franzandel.dicodingintermediatesubmission.helper.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Franz Andel
 * on 28 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @Mock
    private lateinit var context: Context

    private lateinit var detailViewModel: DetailViewModel
    private val coroutineThread: CoroutineThread = CoroutineThreadImpl()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(coroutineThread)
    }

    @Test
    fun `when getLocation triggers nothing`() = runTest {
        detailViewModel.getLocation(context = context, latitude = null, longitude = null)
        Assert.assertNull(detailViewModel.location.value)
    }

    @Test
    fun `when getLocation not found`() = runTest {
        val expectedIsLocationAvailable = false
        detailViewModel.getLocation(context = context, latitude = 0.0, longitude = 0.0)
        val actualIsLocationAvailable = detailViewModel.isLocationAvailable.getOrAwaitValue()
        Assert.assertEquals(expectedIsLocationAvailable, actualIsLocationAvailable)
    }
}
