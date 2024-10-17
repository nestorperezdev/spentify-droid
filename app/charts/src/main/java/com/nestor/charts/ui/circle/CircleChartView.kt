package com.nestor.charts.ui.circle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nestor.charts.data.circle.CircleChartData
import com.nestor.charts.ui.common.ChartFooter
import com.nestor.charts.ui.common.ChartHeaderView

@Composable
fun CircleChartView(
    modifier: Modifier,
    data: CircleChartData
) {
    Card(onClick = { /*TODO*/ }) {
        Column(modifier = modifier, verticalArrangement = spacedBy(24.dp)) {
            ChartHeaderView(data = data.header)
            CircleChartContentView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4 / 3f),
                data = data
            )
            ChartFooter(data = data)
        }
    }
}

@Composable
fun CircleChartContentView(modifier: Modifier = Modifier, data: CircleChartData) {
    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val arcSize = if (size.width < size.height) size.width else size.height
            val startLeft = size.width / 2 - arcSize / 2
            val startTop = size.height / 2 - arcSize / 2
            var previousAngle = 0f
            data.series.forEach { serie ->
                val sweepAngle = data.getCorrespondingSeriesAngle(serie)
                drawArc(
                    color = Color(serie.color),
                    useCenter = true,
                    startAngle = previousAngle,
                    sweepAngle = sweepAngle,
                    size = Size(arcSize, arcSize),
                    topLeft = Offset(startLeft, startTop)
                )
                previousAngle += sweepAngle
            }
        }
    }
}
