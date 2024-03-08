package com.nestor.dashboard.data

import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.DashboardQuery
import com.nestor.schema.type.SummaryDashboardInput

interface DashboardRemoteDataSource {
    suspend fun fetchDashboardInfo(context: SummaryDashboardInput): ApolloResponse<DashboardQuery.Data>
}