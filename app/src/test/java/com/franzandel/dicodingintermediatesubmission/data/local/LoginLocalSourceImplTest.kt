package com.franzandel.dicodingintermediatesubmission.data.local

import androidx.datastore.core.DataStore
import com.example.application.AuthenticationSession
import com.franzandel.dicodingintermediatesubmission.core.model.Result
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
 * on 02 August 2022.
 */

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginLocalSourceImplTest {

    @Mock
    private lateinit var authenticationSession: AuthenticationSession

    @Mock
    private lateinit var datastore: DataStore<AuthenticationSession>

    private lateinit var loginLocalSource: LoginLocalSource

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        loginLocalSource = LoginLocalSourceImpl(datastore)
    }

    @Test
    fun `when saveToken return success`() = runTest {
        val fakeToken = "asdf"
        val fakeResponse = Unit

        val actualToken = loginLocalSource.saveToken(fakeToken)
        Assert.assertNotNull(actualToken)
        Assert.assertTrue(actualToken is Result.Success)
        Assert.assertEquals(fakeResponse, (actualToken as Result.Success).data)
    }

    @Test
    fun `when getToken return success`() = runTest {
        val fakeToken = "asdf"
        Mockito.`when`(authenticationSession.token).thenReturn(fakeToken)
        Mockito.`when`(datastore.data).thenReturn(flowOf(authenticationSession))

        val actualToken = loginLocalSource.getToken()
        Mockito.verify(datastore).data
        Assert.assertNotNull(actualToken)
        Assert.assertTrue(actualToken is Result.Success)
        Assert.assertEquals(fakeToken, (actualToken as Result.Success).data.first())
    }
}
