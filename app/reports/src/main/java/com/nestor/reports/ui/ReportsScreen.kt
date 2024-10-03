package com.nestor.reports.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.nestor.charts.data.bar.grouped.GroupedBarData
import com.nestor.charts.ui.bar.GroupedChartBarView
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ReportsScreen(
    modifier: Modifier = Modifier,
    viewModel: ReportsViewModel = hiltViewModel()
) {
    ReportsScreenContent(
        modifier = modifier,
        stackedChart = viewModel.stackedChartState
    )
}

@Composable
fun ReportsScreenContent(
    modifier: Modifier = Modifier,
    stackedChart: StateFlow<ResponseWrapper<GroupedBarData>>
) {
    Column(modifier = modifier) {
        Text(text = "Reports Screen")
        StackedReportContainer(
            modifier = Modifier.fillMaxWidth(),
            data = stackedChart
        )
    }
}

@Composable
private fun StackedReportContainer(
    modifier: Modifier = Modifier,
    data: StateFlow<ResponseWrapper<GroupedBarData>>
) {
    val response = data.collectAsState().value
    Column(modifier = modifier) {
        when {
            response.isLoading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator()
                }
            }

            response.isSuccessful() -> {
                response.body?.let {
                    GroupedChartBarView(
                        modifier = Modifier.fillMaxSize(),
                        data = it
                    )
                }
            }

            else -> {
                Text(
                    text = response.error ?: "Error fetching data",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}