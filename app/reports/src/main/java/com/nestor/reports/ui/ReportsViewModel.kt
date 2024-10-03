package com.nestor.reports.ui

import androidx.lifecycle.ViewModel
import com.nestor.charts.data.bar.grouped.GroupedBarData
import com.nestor.schema.utils.ResponseWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReportsViewModel: ViewModel() {
    val _stackedChartState =
        MutableStateFlow<ResponseWrapper<GroupedBarData>>(ResponseWrapper.loading())
    val stackedChartState = _stackedChartState.asStateFlow()
}