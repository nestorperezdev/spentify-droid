package com.nestor.auth.di

import com.apollographql.apollo3.ApolloClient
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AuthModule::class])
interface AuthComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun apolloClient(apolloClient: ApolloClient): Builder
        fun build(): AuthComponent
    }
}