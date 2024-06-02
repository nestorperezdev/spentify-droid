package com.nestor.dashboard.data

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.nestor.common.data.auth.AuthRepository
import com.nestor.common.data.auth.datasource.AuthLocalDataSource
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDashboardDataSource,
    private val remoteDataSource: DashboardRemoteDataSource,
    private val authRepo: AuthRepository,
    private val authLocalDataSource: AuthLocalDataSource
) : DashboardRepository {
    override fun fetchDashboardInfo(): Flow<ResponseWrapper<DashboardEntity>> =
        flow {
            authLocalDataSource.userDetails().filterNotNull().collect { details ->
                localDataSource.getCurrentDashboard(details.uuid).collect { dashboard ->
                    dashboard?.let {
                        emit(ResponseWrapper(body = it))
                    } ?: run {
                        emit(ResponseWrapper(isLoading = true))
                        refreshDashboardData()
                    }
                }
            }
        }

    override suspend fun refreshDashboardData() {
        try {
            val dashboardResponse =
                remoteDataSource.fetchDashboardInfo(localDataSource.getSummaryContext())
            dashboardResponse.data?.dashboard?.let { response ->
                authRepo.userDetails().filterNotNull().filter { it.body != null }.take(1).collectLatest {
                    it.body?.let {
                        localDataSource.insertDashboard(
                            DashboardEntity(
                                userUuid = it.uuid,
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
            }
        } catch (e: ApolloNetworkException) {
            e.printStackTrace()
        }
    }
}