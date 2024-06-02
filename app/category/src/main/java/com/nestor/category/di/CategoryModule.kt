package com.nestor.category.di

import com.nestor.category.data.category.CategoryLocalDataSource
import com.nestor.category.data.category.CategoryLocalDataSourceImpl
import com.nestor.category.data.subcategory.SubcategoryLocalDataSource
import com.nestor.category.data.subcategory.SubcategoryLocalDataSourceImpl
import com.nestor.category.data.subcategory.SubcategoryRemoteDataSource
import com.nestor.category.data.subcategory.SubcategoryRemoteDataSourceImpl
import com.nestor.category.data.subcategory.SubcategoryRepository
import com.nestor.category.data.subcategory.SubcategoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CategoryModule {
    @Binds
    @Singleton
    fun bindsSubcategoryRepository(impl: SubcategoryRepositoryImpl): SubcategoryRepository
    @Binds
    @Singleton
    fun bindsSubcategoryLocalDataSource(impl: SubcategoryLocalDataSourceImpl): SubcategoryLocalDataSource
    @Binds
    @Singleton
    fun bindsSubcategoryRemoteDataSource(impl: SubcategoryRemoteDataSourceImpl): SubcategoryRemoteDataSource

    @Binds
    @Singleton
    fun bindsCategoryLocalDataSource(impl: CategoryLocalDataSourceImpl): CategoryLocalDataSource

}