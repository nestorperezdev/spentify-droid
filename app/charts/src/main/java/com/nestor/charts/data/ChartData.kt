package com.nestor.charts.data

import com.nestor.charts.data.common.ChartHeader

open class ChartData(
    open val header: ChartHeader,
    open val series: List<ChartSeries>
)