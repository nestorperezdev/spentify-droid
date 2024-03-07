package com.nestor.dashboard.di

import com.nestor.dashboard.data.DashboardRemoteDataSource
import com.nestor.dashboard.data.DashboardRemoteDataSourceImpl
import com.nestor.dashboard.data.DashboardRepository
import com.nestor.dashboard.data.DashboardRepositoryImpl
import com.nestor.dashboard.data.LocalDashboardDataSource
import com.nestor.dashboard.data.LocalDashboardDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DashboardModule {
    @Binds
    @Singleton
    fun bindsDashboardRemoteDataSource(remoteDataSourceImpl: DashboardRemoteDataSourceImpl): DashboardRemoteDataSource

    @Binds
    @Singleton
    fun bindsDashboardLocalDataSource(localDashboardDataSourceImpl: LocalDashboardDataSourceImpl): LocalDashboardDataSource

    @Binds
    @Singleton
    fun bindsDashboardRepository(repositoryImpl: DashboardRepositoryImpl): DashboardRepository
}