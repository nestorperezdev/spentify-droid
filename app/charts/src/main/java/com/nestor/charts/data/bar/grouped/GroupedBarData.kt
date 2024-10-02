package com.nestor.charts.data.bar.grouped

import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.bar.ChartBarHeader
import com.nestor.charts.data.common.GroupableByTagAndColor

class GroupedBarData(
    val header: ChartBarHeader,
    val series: List<GroupedSeries>
): GroupableByTagAndColor {
    data class GroupedSeries(
        val seriesTitle: String,
        val series: List<ChartSeries>
    )

    override fun groupByTagAndColor(): Map<Pair<String, Int>, List<ChartSeries>> {
        return series.flatMap { it.series }.groupBy { Pair(it.tag, it.color) }
    }
}