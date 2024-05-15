package com.nestor.common.data.auth

import app.cash.turbine.test
import com.nestor.common.data.auth.datasource.AuthLocalDataSource
import com.nestor.common.data.auth.datasource.AuthRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class AuthRepositoryImplTest {
    @MockK
    private lateinit var localDataSource: AuthLocalDataSource
    @MockK
    private lateinit var remoteDataSource: AuthRemoteDataSource
    private lateinit var sut: AuthRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        sut = AuthRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `user details`() = runBlocking {
        sut.userDetails().test {

        }
    }
}