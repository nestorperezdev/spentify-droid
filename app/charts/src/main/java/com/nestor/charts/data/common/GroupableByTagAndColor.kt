package com.nestor.charts.data.common

import com.nestor.charts.data.ChartSeries

interface GroupableByTagAndColor {
    fun groupByTagAndColor(): Map<Pair<String, Int>, List<ChartSeries>>
}