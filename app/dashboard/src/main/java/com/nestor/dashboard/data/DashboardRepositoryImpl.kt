package com.nestor.dashboard.data

import com.apollographql.apollo3.exception.ApolloNetworkException
import com.nestor.auth.data.datasource.AuthLocalDataSource
import com.nestor.dashboard.ui.UserDetails
import com.nestor.database.data.dashboard.DashboardEntity
import com.nestor.database.data.user.UserEntity
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.uikit.util.CoroutineContextProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDashboardDataSource,
    private val remoteDataSource: DashboardRemoteDataSource,
    private val coroutineProvider: CoroutineContextProvider,
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
                        refreshDashboardData(details)
                    }
                }
            }
        }

    override suspend fun refreshDashboardData(userEntity: UserEntity) {
        try {
            val dashboardResponse =
                remoteDataSource.fetchDashboardInfo(localDataSource.getSummaryContext())
            dashboardResponse.data?.dashboard?.let { response ->
                localDataSource.insertDashboard(
                    DashboardEntity(
                        userUuid = userEntity.uuid,
                        userName = response.firstName,
                        dailyPhrase = response.dailyPhrase,
                        totalExpenses = response.summary.totalExpenses,
                        dailyAverageExpense = response.summary.dailyAverageExpense,
                        minimalExpense = response.summary.minimalExpense,
                        maximalExpense = response.summary.maximalExpense
                    )
                )
            }
        } catch (e: ApolloNetworkException) {
            e.printStackTrace()
        }
    }
}