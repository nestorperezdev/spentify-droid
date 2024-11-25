package com.nestor.reports.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.bar.grouped.GroupedBarData
import com.nestor.charts.data.circle.CircleChartData
import com.nestor.charts.data.common.ChartHeaderData
import com.nestor.common.data.auth.AuthRepository
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.catergory.CategoryWithSubcategory
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.expense.ExpenseWithCategoryAndSubcategory
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import com.nestor.database.data.subcategory.SubCategoryEntity
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    authRepository: AuthRepository
) :
    ViewModel() {
    private val _selectedDate = MutableStateFlow(Date())
    private val _expenseDataInMonth = _selectedDate
        .combineTransform(authRepository.userDetails()) { date, userWrapper ->
            emitAll(expenseRepository.getExpenses(date).map { ResponseWrapper.success(it) })
        }
    private val _expenseWithCatAndSubInMonth = _selectedDate
        .combineTransform<Date, ResponseWrapper<UserEntity?>, ResponseWrapper<List<ExpenseWithCategoryAndSubcategory>>>(
            authRepository.userDetails()
        ) { date, userWrapper ->
            if (userWrapper.isLoading) emit(ResponseWrapper.loading(null))
            userWrapper.error?.let { emit(ResponseWrapper.error(it)) }
            userWrapper.body?.let { user ->
                emitAll(
                    expenseRepository.getExpensesWithCategoryAndSubcategory(date)
                        .map { ResponseWrapper.success(it) }
                )
            }
        }
    val categorizedCircleChartDataState: StateFlow<ResponseWrapper<List<CircleChartData>>> =
        flow<ResponseWrapper<List<CircleChartData>>> { emit(ResponseWrapper.loading()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ResponseWrapper.loading()
            )
    val stackedChartState: StateFlow<ResponseWrapper<GroupedBarData>> =
        _expenseWithCatAndSubInMonth
            .map { it.toGroupedBarData() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ResponseWrapper.loading(null)
            )
    val circleChartDataState: StateFlow<ResponseWrapper<CircleChartData>> =
        _expenseDataInMonth
            .map { it.toCircleChartData() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ResponseWrapper.loading(null)
            )

    private fun ResponseWrapper<List<ExpenseWithCategoryEntity>>.toCircleChartData(): ResponseWrapper<CircleChartData> {
        return mapBody { list ->
            val groupedList: Map<CategoryEntity?, List<ExpenseEntity>> = list
                .filter { it.category != null }
                .groupBy { it.category }
                .mapValues { it.value.map { expWCat -> expWCat.expense } }
            CircleChartData(
                header = ChartHeaderData(
                    chartName = AnnotatedString("Chart Name"),
                    hint = AnnotatedString("Hint"),
                    chartDescription = AnnotatedString("Description"),
                    total = AnnotatedString("$100")
                ),
                series = groupedList.map { (category, expenses) ->
                    ChartSeries(
                        tag = category?.name ?: "No Category",
                        color = category?.tint?.or(0xFF000000.toInt()) ?: 0xFF000000.toInt(),
                        value = expenses.sumOf { it.amount }.toFloat()
                    )
                }
            )
        }
    }

    private fun ResponseWrapper<List<ExpenseWithCategoryAndSubcategory>>.toGroupedBarData(): ResponseWrapper<GroupedBarData> {
        return mapBody { list ->
            GroupedBarData(
                header = ChartHeaderData(
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

    private fun groupExpenses(list: List<ExpenseWithCategoryAndSubcategory>): List<GroupedBarData.GroupedSeries> {
        val groupedSeries =
            mutableMapOf<SubCategoryEntity, MutableMap<CategoryEntity, MutableList<ExpenseEntity>>>()
        list.forEach { expWCatAndSubcat ->
            val categoryMap = groupedSeries.getOrDefault(
                expWCatAndSubcat.category.subcategoryEntity,
                mutableMapOf()
            )
            groupedSeries[expWCatAndSubcat.category.subcategoryEntity] = categoryMap
            categoryMap.getOrDefault(expWCatAndSubcat.category.categoryEntity, mutableListOf())
                .apply {
                    add(expWCatAndSubcat.expense)
                    categoryMap[expWCatAndSubcat.category.categoryEntity] = this
                }
        }
        return groupedSeries.map { (subcategory, categoryExpenseMap) ->
            GroupedBarData.GroupedSeries(
                seriesTitle = subcategory.name,
                series = categoryExpenseMap.map { (category, expenses) ->
                    ChartSeries(
                        tag = category.name,
                        color = category.tint?.or(0xFF000000.toInt()) ?: 0xFF000000.toInt(),
                        value = expenses.sumOf { it.amount }.toFloat()
                    )
                }
            )
        }
    }
}
