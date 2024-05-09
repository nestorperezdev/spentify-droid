package com.nestor.common.di

import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.auth.AuthRepositoryImpl
import com.nestor.common.data.auth.datasource.AuthLocalDataSource
import com.nestor.common.data.auth.datasource.AuthLocalDataSourceImpl
import com.nestor.common.data.auth.datasource.AuthRemoteDataSource
import com.nestor.common.data.auth.datasource.AuthRemoteDataSourceImpl
import com.nestor.common.data.auth.datasource.AuthTokenInterceptor
import com.nestor.common.data.auth.datasource.AuthTokenInterceptorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
    @Binds
    @Singleton
    fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    fun bindAuthLocalDataSource(
        authLocalDataSourceImpl: AuthLocalDataSourceImpl
    ): AuthLocalDataSource

    @Binds
    @Singleton
    fun bindAuthRemoteDataSource(
        authRemoteDataSource: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    fun bindAuthTokenInterceptor(
        impl: AuthTokenInterceptorImpl
    ): AuthTokenInterceptor
}
