package com.nestor.reports.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.bar.ChartBarHeader
import com.nestor.charts.data.bar.grouped.GroupedBarData
import com.nestor.common.data.auth.AuthRepository
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryDao
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import com.nestor.database.data.user.UserEntity
import com.nestor.expenses.data.ExpenseRepository
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.mapBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val authRepository: AuthRepository
) :
    ViewModel() {
    private val _selectedDate = MutableStateFlow(Date())
    val stackedChartState: StateFlow<ResponseWrapper<GroupedBarData>> =
        _selectedDate
            .combineTransform<Date, ResponseWrapper<UserEntity?>, ResponseWrapper<List<ExpenseWithCategoryEntity>>>(
                authRepository.userDetails()
            ) { date, userWrapper ->
                val calendar = Calendar.getInstance()
                calendar.time = date
                if (userWrapper.isLoading) emit(ResponseWrapper.loading(null))
                userWrapper.error?.let { emit(ResponseWrapper.error(it)) }
                userWrapper.body?.let { user ->
                    emitAll(
                        expenseRepository.getExpenses(
                            month = calendar.get(Calendar.MONTH) + 1,
                            year = calendar.get(Calendar.YEAR),
                            userUid = user.uuid,
                            currencyCode = user.currencyCode
                        ).map { ResponseWrapper.success(it) }
                    )
                }
            }
            .map {
                it.mapBody { list ->
                    GroupedBarData(
                        header = ChartBarHeader(
                            chartName = AnnotatedString("Chart Name"),
                            hint = AnnotatedString("Hint"),
                            chartDescription = AnnotatedString("Description"),
                            total = AnnotatedString("$100")
                        ),
                        style = GroupedBarData.Companion.GroupedBarStyle.STACKED,
                        series = groupExpenses(list)
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ResponseWrapper.loading(null)
            )

    private fun groupExpenses(list: List<ExpenseWithCategoryEntity>): List<GroupedBarData.GroupedSeries> {
        return list.groupBy { it.category?.name }
            .map { (category, expenses) ->
                GroupedBarData.GroupedSeries(
                    seriesTitle = category ?: "Unknown",
                    series = expenses.map {
                        ChartSeries(
                            value = it.expense.usdValue.toFloat(),
                            tag = it.category?.name ?: "Unknown",
                            color = it.category?.tint ?: 0xFFEFB8C8.toInt()
                        )
                    },
                )
            }
    }
}