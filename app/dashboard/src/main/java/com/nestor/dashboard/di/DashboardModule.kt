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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface DashboardModule {
    @Binds
    @ViewModelScoped
    fun bindsDashboardRemoteDataSource(remoteDataSourceImpl: DashboardRemoteDataSourceImpl): DashboardRemoteDataSource

    @Binds
    @ViewModelScoped
    fun bindsDashboardLocalDataSource(localDashboardDataSourceImpl: LocalDashboardDataSourceImpl): LocalDashboardDataSource

    @Binds
    @ViewModelScoped
    fun bindsDashboardRepository(repositoryImpl: DashboardRepositoryImpl): DashboardRepository
}