package com.nestor.onboarding.di

import com.nestor.onboarding.data.datasource.OnboardingLocalDataSource
import com.nestor.onboarding.data.datasource.OnboardingLocalDataSourceImpl
import com.nestor.onboarding.data.repository.OnboardingRepository
import com.nestor.onboarding.data.repository.OnboardingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface OnboardingModule {
    @Binds
    @ViewModelScoped
    fun bindsOnboardingLocalDataSource(dataSourceImpl: OnboardingLocalDataSourceImpl): OnboardingLocalDataSource

    @Binds
    @ViewModelScoped
    fun bindsOnboardingRepository(repositoryImpl: OnboardingRepositoryImpl): OnboardingRepository
}