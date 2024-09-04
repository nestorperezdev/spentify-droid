package com.nestor.expenses.di

import com.nestor.expenses.data.ExpenseLocalDataSource
import com.nestor.expenses.data.ExpenseLocalDataSourceImpl
import com.nestor.expenses.data.ExpenseRemoteDataSource
import com.nestor.expenses.data.ExpenseRemoteDataSourceImpl
import com.nestor.expenses.data.ExpenseRepository
import com.nestor.expenses.data.ExpenseRepositoryImpl
import com.nestor.expenses.data.expensewithcategory.ExpenseWithCategoryLocalDataSource
import com.nestor.expenses.data.expensewithcategory.ExpenseWithCategoryLocalDataSourceImpl
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
    fun bindsExpenseLocalDataSource(localDataSource: ExpenseLocalDataSourceImpl): ExpenseLocalDataSource

    @Binds
    @ViewModelScoped
    fun bindsExpenseRepository(repository: ExpenseRepositoryImpl): ExpenseRepository

    @Binds
    @ViewModelScoped
    fun bindsExpenseWithCategoryLocalDataSource(repository: ExpenseWithCategoryLocalDataSourceImpl): ExpenseWithCategoryLocalDataSource
}