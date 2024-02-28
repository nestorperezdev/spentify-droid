package com.nestor.onboarding.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val ONBOARDING = booleanPreferencesKey("onboarding")

class OnboardingLocalDataSourceImpl @Inject constructor(
    private val appDataStore: DataStore<Preferences>
) : OnboardingLocalDataSource {

    override fun isOnboardingComplete() =
        appDataStore.data.map { prefs -> prefs[ONBOARDING] ?: false }

    override suspend fun setOnBoardingComplete() {
        appDataStore.edit { it[ONBOARDING] = true }
    }
}