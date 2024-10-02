package com.nestor.charts.data.bar

import androidx.compose.ui.text.AnnotatedString
import com.nestor.charts.data.ChartData
import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.common.ChartHeader
import com.nestor.charts.data.common.GroupableByTagAndColor

class ChartBarHeader(
    override val chartName: AnnotatedString,
    override val chartDescription: AnnotatedString,
    val hint: AnnotatedString? = null,
    val total: AnnotatedString? = null,
) : ChartHeader(chartName, chartDescription)

open class BarData(
    override val header: ChartBarHeader,
    override val series: List<BarSeries>,
) : ChartData(header, series), GroupableByTagAndColor {
    data class BarSeries(
        override val color: Int,
        override val tag: String,
        override val value: Float,
        val seriesTitle: String
    ) : ChartSeries(color, tag, value)

    override fun groupByTagAndColor(): Map<Pair<String, Int>, List<ChartSeries>> {
        return series.groupBy { Pair(it.tag, it.color) }
    }
}
