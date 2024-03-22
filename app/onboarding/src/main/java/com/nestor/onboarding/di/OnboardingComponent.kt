package com.nestor.onboarding.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.BindsInstance
import dagger.Component


@Component(modules = [OnboardingModule::class])
interface OnboardingComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun dataStore(appDataStore: DataStore<Preferences>): Builder
        fun build(): OnboardingComponent
    }
}