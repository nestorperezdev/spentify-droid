package com.nestor.charts.data.bar

import com.nestor.charts.data.ChartData
import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.common.ChartHeader

class ChartBarHeader(
    override val chartName: String,
    override val chartDescription: String,
    val hint: String? = null,
    val total: String? = null,
) : ChartHeader(chartName, chartDescription)

open class BarData(
    override val header: ChartBarHeader,
    override val series: List<BarSeries>,
) : ChartData(header, series) {
    data class BarSeries(
        override val color: Int,
        override val tag: String,
        override val value: Float,
        val seriesTitle: String
    ) : ChartSeries(color, tag, value)

    internal fun groupSeriesByTagAndColor(): Map<Pair<String, Int>, List<ChartSeries>> {
        return series.groupBy { Pair(it.tag, it.color) }
    }
}
