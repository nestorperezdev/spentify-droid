package com.nestor.onboarding.data.repository

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun isOnboardingComplete(): Flow<Boolean>
    suspend fun setOnBoardingComplete()
}