package com.nestor.common.di

import com.nestor.common.data.CurrencyLocalDataSource
import com.nestor.common.data.CurrencyLocalDataSourceImpl
import com.nestor.common.data.CurrencyRemoteDataSource
import com.nestor.common.data.CurrencyRemoteDataSourceImpl
import com.nestor.common.data.CurrencyRepository
import com.nestor.common.data.CurrencyRepositoryImpl
import com.nestor.common.data.statusbar.StatusBarRepository
import com.nestor.common.data.statusbar.StatusBarRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CommonModule {
    @Binds
    @Singleton
    fun bindsCurrencyRepository(currencyRepository: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    @Singleton
    fun bindsCurrencyLocalDataSource(currencyLocalDataSource: CurrencyLocalDataSourceImpl): CurrencyLocalDataSource

    @Binds
    @Singleton
    fun bindsCurrencyRemoteDataSource(currencyRemoteDataSource: CurrencyRemoteDataSourceImpl): CurrencyRemoteDataSource

    @Binds
    @Singleton
    fun bindsStatusBarRepository(statusBarRepository: StatusBarRepositoryImpl): StatusBarRepository
}