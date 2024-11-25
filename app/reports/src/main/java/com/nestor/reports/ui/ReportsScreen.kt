package com.nestor.reports.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.charts.data.bar.grouped.GroupedBarData
import com.nestor.charts.data.circle.CircleChartData
import com.nestor.charts.ui.bar.GroupedChartBarView
import com.nestor.charts.ui.circle.CircleChartView
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ReportsScreen(
    modifier: Modifier = Modifier,
    viewModel: ReportsViewModel = hiltViewModel()
) {
    ReportsScreenContent(
        modifier = modifier,
        stackedChart = viewModel.stackedChartState,
        circleChartData = viewModel.circleChartDataState,
        categorizedCircleChartData = viewModel.categorizedCircleChartDataState
    )
}

@Composable
fun ReportsScreenContent(
    modifier: Modifier = Modifier,
    stackedChart: StateFlow<ResponseWrapper<GroupedBarData>>,
    circleChartData: StateFlow<ResponseWrapper<CircleChartData>>,
    categorizedCircleChartData: StateFlow<ResponseWrapper<List<CircleChartData>>>
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = spacedBy(16.dp)
    ) {
        StackedReportContainer(
            modifier = Modifier.fillMaxWidth(),
            data = stackedChart
        )
        DonutCategoryReport(
            modifier = Modifier.fillMaxWidth(),
            data = circleChartData
        )
        DonutCategoriesReport(categorizedCircleChartData)
    }
}

@Composable
private fun ColumnScope.DonutCategoriesReport(response: StateFlow<ResponseWrapper<List<CircleChartData>>>) {
    val state = response.collectAsState().value
    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        state.isSuccessful() -> {
            state.body?.forEach { report ->
                DonutCategoryReportContent(
                    modifier = Modifier.fillMaxWidth(),
                    response = ResponseWrapper.success(report)
                )
            }
        }

        else -> {
            Text(
                text = state.error ?: "Error fetching data",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun DonutCategoryReport(
    modifier: Modifier = Modifier,
    data: StateFlow<ResponseWrapper<CircleChartData>>
) {
    val response = data.collectAsState().value
    DonutCategoryReportContent(modifier = modifier, response = response)
}

@Composable
private fun DonutCategoryReportContent(
    modifier: Modifier,
    response: ResponseWrapper<CircleChartData>
) {
    when {
        response.isLoading -> {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        response.isSuccessful() -> {
            response.body?.let {
                CircleChartView(
                    modifier = modifier.fillMaxWidth(),
                    data = it
                )
            }
        }

        else -> {
            Text(
                text = response.error ?: "Error fetching data",
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun StackedReportContainer(
    modifier: Modifier = Modifier,
    data: StateFlow<ResponseWrapper<GroupedBarData>>
) {
    val response = data.collectAsState().value
    when {
        response.isLoading -> {
            Box(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator()
            }
        }

        response.isSuccessful() -> {
            response.body?.let {
                GroupedChartBarView(
                    modifier = modifier.fillMaxWidth(),
                    data = it
                )
            }
        }

        else -> {
            Text(
                text = response.error ?: "Error fetching data",
                modifier = modifier.fillMaxSize()
            )
        }
    }
}