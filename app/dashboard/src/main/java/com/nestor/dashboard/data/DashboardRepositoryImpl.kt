package com.nestor.dashboard.data

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.nestor.auth.data.datasource.AuthLocalDataSource
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.util.CoroutineContextProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDashboardDataSource,
    private val remoteDataSource: DashboardRemoteDataSource,
    private val coroutineProvider: CoroutineContextProvider,
    private val authLocalDataSource: AuthLocalDataSource
) : DashboardRepository {
    override fun fetchDashboardInfo(userUuid: String): Flow<ResponseWrapper<DashboardEntity>> =
        flow {
            combine(
                authLocalDataSource.userDetailsFlow().filterNotNull(),
                localDataSource.getCurrentDashboard(userUuid)
            ) { details, dashboard ->
                dashboard?.let {
                    emit(ResponseWrapper(body = it))
                } ?: run {
                    emit(ResponseWrapper(isLoading = true))
                    refreshDashboardData()
                }
            }.collect()
        }

    override fun refreshDashboardData() {
        runBlocking(coroutineProvider.io()) {
            try {
                val dashboardResponse =
                    remoteDataSource.fetchDashboardInfo(localDataSource.getSummaryContext())
                dashboardResponse.data?.dashboard?.let { response ->
                    authLocalDataSource.userDetails()?.let {
                        localDataSource.insertDashboard(
                            DashboardEntity(
                                userUuid = it.sub,
                                userName = response.firstName,
                                dailyPhrase = response.dailyPhrase,
                                totalExpenses = response.summary.totalExpenses,
                                dailyAverageExpense = response.summary.dailyAverageExpense,
                                minimalExpense = response.summary.minimalExpense,
                                maximalExpense = response.summary.maximalExpense
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