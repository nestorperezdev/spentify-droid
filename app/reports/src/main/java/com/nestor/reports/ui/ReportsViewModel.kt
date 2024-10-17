package com.nestor.reports.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.bar.ChartBarHeader
import com.nestor.charts.data.bar.grouped.GroupedBarData
import com.nestor.common.data.auth.AuthRepository
import com.nestor.database.data.catergory.CategoryEntity
import com.nestor.database.data.expense.ExpenseEntity
import com.nestor.database.data.expense.ExpenseWithCategoryAndSubcategory
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryDao
import com.nestor.database.data.expensewithcategory.ExpenseWithCategoryEntity
import com.nestor.database.data.subcategory.SubCategoryEntity
import com.nestor.database.data.user.UserEntity
import com.nestor.expenses.data.ExpenseRepository
import com.nestor.schema.type.User
import com.nestor.schema.utils.ResponseWrapper
import com.nestor.schema.utils.combineTransformNonWrapper
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
    authRepository: AuthRepository
) :
    ViewModel() {
    private val _selectedDate = MutableStateFlow(Date())
    val stackedChartState: StateFlow<ResponseWrapper<GroupedBarData>> =
        _selectedDate
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
            .map { it.toGroupedBarData() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = ResponseWrapper.loading(null)
            )

    private fun ResponseWrapper<List<ExpenseWithCategoryAndSubcategory>>.toGroupedBarData(): ResponseWrapper<GroupedBarData> {
        return mapBody { list ->
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
