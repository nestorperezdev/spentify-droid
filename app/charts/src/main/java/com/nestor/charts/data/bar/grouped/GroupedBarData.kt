package com.nestor.charts.data.bar.grouped

import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.bar.ChartBarHeader
import com.nestor.charts.data.common.GroupableByTagAndColor

class GroupedBarData(
    val header: ChartBarHeader,
    val series: List<GroupedSeries>,
    val style: GroupedBarStyle = GroupedBarStyle.GROUPED
) : GroupableByTagAndColor {
    data class GroupedSeries(
        val seriesTitle: String,
        val series: List<ChartSeries>
    )

    override fun groupByTagAndColor(): Map<Pair<String, Int>, List<ChartSeries>> {
        return series.flatMap { it.series }.groupBy { Pair(it.tag, it.color) }
    }

    fun calculateMaxValue(): Float {
        if (style == GroupedBarStyle.GROUPED) {
            return series.flatMap { it.series }.maxOfOrNull { it.value } ?: 0f
        } else {
            return series.map { it.series.map { it.value }.sum() }.maxOrNull() ?: 0f
        }
    }

    companion object {
        enum class GroupedBarStyle {
            STACKED,
            GROUPED
        }
    }
}