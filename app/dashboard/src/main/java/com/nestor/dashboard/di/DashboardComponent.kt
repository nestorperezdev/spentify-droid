package com.nestor.dashboard.di

import dagger.Component

@Component(modules = [DashboardModule::class])
interface DashboardComponent {
    @Component.Builder
    interface Builder {
        fun build(): DashboardComponent
    }
}