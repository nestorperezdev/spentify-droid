package com.nestor.spentify.di

import android.content.Context
import android.icu.util.Calendar
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.nestor.common.data.auth.datasource.AuthTokenInterceptor
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
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.apollographql.apollo3.adapter.DateAdapter
import com.nestor.common.data.appinfo.AppInfoDataSource
import com.nestor.common.data.monthandyear.MonthAndYear
import com.nestor.schema.type.DateTime
import com.nestor.spentify.data.appinfo.AppInfoDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun coroutineContextProvider(
        contextProviderImpl: CoroutineContextProviderImpl
    ): CoroutineContextProvider

    @Binds
    @Singleton
    abstract fun bindsAppInfoDataSource(impl: AppInfoDataSourceImpl): AppInfoDataSource

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
                .okHttpClient(
                    OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build()
                )
                .addCustomScalarAdapter(DateTime.type, DateAdapter)
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

        @Provides
        @Singleton
        fun providesCurrentMonthAndYear(): MonthAndYear {
            val calendar = Calendar.getInstance()
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)
            return MonthAndYear(month, year)
        }
    }
}