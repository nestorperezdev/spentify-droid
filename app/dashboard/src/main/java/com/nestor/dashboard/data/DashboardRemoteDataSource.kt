package com.nestor.dashboard.data

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.DashboardQuery

interface DashboardRemoteDataSource {
    suspend fun fetchDashboardInfo(): ApolloResponse<DashboardQuery.Data>
}