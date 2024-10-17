package com.nestor.charts.data.circle

import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.common.ChartHeaderData
import com.nestor.charts.data.common.GroupableByTagAndColor

data class CircleChartData(
    val header: ChartHeaderData,
    val series: List<ChartSeries>
) : GroupableByTagAndColor {
    val maxValue: Float by lazy { series.map { it.value }.sum() }

    fun getCorrespondingSeriesAngle(serie: ChartSeries): Float {
        return serie.value / maxValue * 360
    }

    override fun groupByTagAndColor(): Map<Pair<String, Int>, List<ChartSeries>> {
        return series.groupBy { Pair(it.tag, it.color) }
    }
}