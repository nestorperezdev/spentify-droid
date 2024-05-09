package com.nestor.common.di

import com.nestor.common.data.currency.CurrencyLocalDataSource
import com.nestor.common.data.currency.CurrencyLocalDataSourceImpl
import com.nestor.common.data.currency.CurrencyRemoteDataSource
import com.nestor.common.data.currency.CurrencyRemoteDataSourceImpl
import com.nestor.common.data.currency.CurrencyRepository
import com.nestor.common.data.currency.CurrencyRepositoryImpl
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