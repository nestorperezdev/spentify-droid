package com.nestor.charts.data.bar

import com.nestor.charts.data.ChartData
import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.common.ChartHeaderData

open class BarData(
    override val header: ChartHeaderData,
    override val series: List<BarSeries>,
) : ChartData(header, series) {
    data class BarSeries(
        override val color: Int,
        override val tag: String,
        override val value: Float,
        val seriesTitle: String
    ) : ChartSeries(color, tag, value)
}
