package com.nestor.charts.data.bar

import com.nestor.charts.data.ChartData
import com.nestor.charts.data.ChartSeries

data class BarData(
    override val chartName: String,
    override val chartDescription: String,
    val hint: String? = null,
    val total: String? = null,
    val series: List<BarSeries>
) : ChartData(chartName, chartDescription) {
    data class BarSeries(
        override val color: Int,
        override val tag: String,
        override val value: Float,
        val seriesTitle: String
    ) : ChartSeries(color, tag, value)

    internal fun groupSeriesByTagAbdColor(): Map<Pair<String, Int>, List<BarSeries>> {
        return series.groupBy { Pair(it.tag, it.color) }
    }
}
