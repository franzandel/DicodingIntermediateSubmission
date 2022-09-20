package com.franzandel.dicodingintermediatesubmission.data.local

import androidx.datastore.core.DataStore
import com.example.application.AuthenticationSession
import com.example.application.HomeSession
import com.franzandel.dicodingintermediatesubmission.core.model.Result
import com.franzandel.dicodingintermediatesubmission.data.dao.HomeDao
import com.franzandel.dicodingintermediatesubmission.data.dao.RemoteKeysDao
import com.franzandel.dicodingintermediatesubmission.helper.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Franz Andel
 * on 27 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeLocalSourceTest {

    @Mock
    private lateinit var homeSession: HomeSession

    @Mock
    private lateinit var settingsDataStore: DataStore<AuthenticationSession>

    @Mock
    private lateinit var homeDatastore: DataStore<HomeSession>

    @Mock
    private lateinit var homeDao: HomeDao

    @Mock
    private lateinit var remoteKeysDao: RemoteKeysDao
    private lateinit var homeLocalSource: HomeLocalSource

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        homeLocalSource =
            HomeLocalSourceImpl(settingsDataStore, homeDatastore, homeDao, remoteKeysDao)
    }

    @Test
    fun `when clearStorage return success`() = runTest {
        val fakeResponse = Unit

        val actualResponse = homeLocalSource.clearStorage()
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(fakeResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `when saveLocationPreference return success`() = runTest {
        val fakeLocation = 0
        val fakeResponse = Unit

        val actualResponse = homeLocalSource.saveLocationPreference(fakeLocation)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is Result.Success)
        Assert.assertEquals(fakeResponse, (actualResponse as Result.Success).data)
    }

    @Test
    fun `when getLocationPreference return success`() = runTest {
        val fakeLocation = 0
        Mockito.`when`(homeSession.locationPreference).thenReturn(fakeLocation)
        Mockito.`when`(homeDatastore.data).thenReturn(flowOf(homeSession))

        val actualLocationPreference = homeLocalSource.getLocationPreference()
        Mockito.verify(homeDatastore).data
        Assert.assertNotNull(actualLocationPreference)
        Assert.assertTrue(actualLocationPreference is Result.Success)
        Assert.assertEquals(fakeLocation, (actualLocationPreference as Result.Success).data.first())
    }
}
