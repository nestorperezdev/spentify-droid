package com.nestor.spentify.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.nestor.spentify.R
import com.nestor.uikit.util.CoroutineContextProvider
import com.nestor.uikit.util.CoroutineContextProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    abstract fun coroutineContextProvider(
        contextProviderImpl: CoroutineContextProviderImpl
    ): CoroutineContextProvider

    companion object {
        @Provides
        @Singleton
        fun providesApolloClient(@ApplicationContext context: Context): ApolloClient {
            return ApolloClient.Builder()
                .serverUrl(context.getString(R.string.graphql_server_url))
                .build()
        }
    }
}