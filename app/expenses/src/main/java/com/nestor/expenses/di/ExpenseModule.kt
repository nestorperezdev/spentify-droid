package com.nestor.expenses.di

import com.nestor.expenses.data.ExpenseRemoteDataSource
import com.nestor.expenses.data.ExpenseRemoteDataSourceImpl
import com.nestor.expenses.data.ExpenseRepository
import com.nestor.expenses.data.ExpenseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface ExpenseModule {
    @Binds
    @ViewModelScoped
    fun bindsExpenseRemoteDataSource(remoteDataSource: ExpenseRemoteDataSourceImpl): ExpenseRemoteDataSource

    @Binds
    @ViewModelScoped
    fun bindsExpenseRepository(repository: ExpenseRepositoryImpl): ExpenseRepository
}