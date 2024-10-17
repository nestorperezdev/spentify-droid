package com.nestor.charts.data.bar.grouped

import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.common.ChartHeaderData
import com.nestor.charts.data.common.GroupableByTagAndColor

class GroupedBarData(
    val header: ChartHeaderData,
    val series: List<GroupedSeries>,
    val style: GroupedBarStyle = GroupedBarStyle.GROUPED
) : GroupableByTagAndColor {
    data class GroupedSeries(
        val seriesTitle: String,
        val series: List<ChartSeries>,
        val style: GroupedBarStyle = GroupedBarStyle.GROUPED
    )

    override fun groupByTagAndColor(): Map<Pair<String, Int>, List<ChartSeries>> {
        return series.flatMap { it.series }.groupBy { Pair(it.tag, it.color) }
    }

    fun calculateMaxValue(): Float {
        if (style == GroupedBarStyle.GROUPED) {
            return series.flatMap { it.series }.maxOf { it.value }
        } else {
            return series.map { it.series.map { it.value }.sum() }.max()
        }
    }

    companion object {
        enum class GroupedBarStyle {
            STACKED,
            GROUPED
        }
    }
}