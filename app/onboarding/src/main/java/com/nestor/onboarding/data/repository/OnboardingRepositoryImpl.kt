package com.nestor.onboarding.data.repository

import com.nestor.onboarding.data.datasource.OnboardingLocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val dataSource: OnboardingLocalDataSource
) : OnboardingRepository {
    override fun isOnboardingComplete(): Flow<Boolean> {
        return this.dataSource.isOnboardingComplete()
    }

    override suspend fun setOnBoardingComplete() {
        this.dataSource.setOnBoardingComplete()
    }
}