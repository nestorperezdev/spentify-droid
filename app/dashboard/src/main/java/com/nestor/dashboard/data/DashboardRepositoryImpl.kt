package com.nestor.dashboard.data

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.nestor.auth.data.datasource.AuthLocalDataSource
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.uikit.util.CoroutineContextProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDashboardDataSource,
    private val remoteDataSource: DashboardRemoteDataSource,
    private val coroutineProvider: CoroutineContextProvider,
    private val authLocalDataSource: AuthLocalDataSource
) : DashboardRepository {
    override fun fetchDashboardInfo(userUuid: String): Flow<DashboardEntity?> {
        return localDataSource.getCurrentDashboard(userUuid)
    }

    override fun refreshDashboardData() {
        runBlocking(coroutineProvider.io()) {
            try {
                val dashboardResponse = remoteDataSource.fetchDashboardInfo()
                dashboardResponse.data?.dashboard?.let { response ->
                    authLocalDataSource.userDetails()?.let {
                        localDataSource.insertDashboard(
                            DashboardEntity(
                                userUuid = it.sub,
                                userName = response.firstName,
                                dailyPhrase = response.dailyPhrase
                            )
                        )
                    }
                }
            } catch (e: ApolloNetworkException) {
                e.printStackTrace()
            }
        }
    }
}