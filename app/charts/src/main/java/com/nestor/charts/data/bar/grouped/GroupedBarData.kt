package com.nestor.charts.data.bar.grouped

import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.bar.ChartBarHeader

class GroupedBarData(
    val header: ChartBarHeader,
    val series: List<GroupedSeries>
) {
    data class GroupedSeries(
        val seriesTitle: String,
        val series: List<ChartSeries>
    )

    fun groupSeriesByTagAndColor(): Map<Pair<String, Int>, List<ChartSeries>> {
        return series.flatMap { it.series }.groupBy { Pair(it.tag, it.color) }
    }
}