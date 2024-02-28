package com.nestor.onboarding.data.datasource

import kotlinx.coroutines.flow.Flow

interface OnboardingLocalDataSource {
    fun isOnboardingComplete(): Flow<Boolean>
    suspend fun setOnBoardingComplete()
}