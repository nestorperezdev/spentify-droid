package com.nestor.spentify.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.apollographql.apollo3.ApolloClient
import com.nestor.auth.data.datasource.AuthTokenInterceptor
import com.nestor.onboarding.data.datasource.OnboardingLocalDataSource
import com.nestor.onboarding.data.datasource.OnboardingLocalDataSourceImpl
import com.nestor.spentify.R
import com.nestor.uikit.util.CoroutineContextProvider
import com.nestor.uikit.util.CoroutineContextProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun coroutineContextProvider(
        contextProviderImpl: CoroutineContextProviderImpl
    ): CoroutineContextProvider

    @Binds
    abstract fun bindsOnboardingLocalDataSource(dataSourceImpl: OnboardingLocalDataSourceImpl): OnboardingLocalDataSource

    companion object {
        @Provides
        @Singleton
        fun providesApolloClient(
            @ApplicationContext context: Context,
            authTokenInterceptor: AuthTokenInterceptor,
        ): ApolloClient {
            return ApolloClient.Builder()
                .serverUrl(context.getString(R.string.graphql_server_url))
                .addHttpInterceptor(authTokenInterceptor)
                .build()
        }

        @Provides
        @Singleton
        fun providesOnboardingDataStore(
            @ApplicationContext appContext: Context,
            dispatcherProvider: CoroutineContextProvider
        ): DataStore<Preferences> {
            return PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
                scope = CoroutineScope(dispatcherProvider.io() + SupervisorJob()),
                produceFile = { appContext.preferencesDataStoreFile("spentify") }
            )
        }
    }
}