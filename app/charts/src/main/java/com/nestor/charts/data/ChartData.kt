package com.nestor.charts.data

import com.nestor.charts.data.common.ChartHeaderData

open class ChartData(
    open val header: ChartHeaderData,
    open val series: List<ChartSeries>
)