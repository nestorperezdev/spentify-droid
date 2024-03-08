package com.nestor.dashboard.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.nestor.schema.DashboardQuery
import com.nestor.schema.type.SummaryDashboardInput
import javax.inject.Inject

class DashboardRemoteDataSourceImpl @Inject constructor(private val apolloClient: ApolloClient) :
    DashboardRemoteDataSource {
    override suspend fun fetchDashboardInfo(context: SummaryDashboardInput): ApolloResponse<DashboardQuery.Data> {
        return apolloClient.query(DashboardQuery(context)).execute()
    }
}