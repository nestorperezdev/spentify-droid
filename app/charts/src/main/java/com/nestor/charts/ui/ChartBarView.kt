package com.nestor.charts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.nestor.charts.data.bar.BarData
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun ChartBarView(modifier: Modifier = Modifier, data: BarData) {
    Column(modifier = modifier, verticalArrangement = spacedBy(24.dp)) {
        Column(verticalArrangement = spacedBy(12.dp)) {
            Text(text = data.chartName, style = MaterialTheme.typography.titleLarge)
            Text(text = data.chartDescription, style = MaterialTheme.typography.titleSmall)
        }
        if (data.hint != null || data.total != null) {
            Column(verticalArrangement = spacedBy(8.dp)) {
                if (data.total != null) {
                    Text(text = data.total, style = MaterialTheme.typography.bodyLarge)
                }
                if (data.hint != null) {
                    Text(text = data.hint, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        //  Canvas here ->
        ChartBarContent(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .heightIn(max = 320.dp),
            data = data
        )
        Row {
            data.groupSeriesByTagAbdColor().forEach { (tagColor: Pair<String, Int>, _) ->
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(color = Color(tagColor.second))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = tagColor.first, style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

@Composable
private fun ChartBarContent(modifier: Modifier, data: BarData) {
    Box(modifier = modifier.fillMaxSize()) {
        // Background
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            repeat(4) { i ->
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .run {
                        if (i == 3) {
                            background(Color.Gray)
                        } else {
                            //  TODO: dashed border line:
                            background(Color.Gray.copy(alpha = 0.4f))
                        }
                    }
                )
            }
        }
        // Bars
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            val maxValue = remember(data.series) { data.series.maxOf { it.value } }
            data.series.forEach { serie ->
                val correspondingHeight = (serie.value / maxValue)
                Box(
                    modifier = Modifier
                        .fillMaxHeight(correspondingHeight)
                        .width(12.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(Color(serie.color))
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 14.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            data.series.forEach { serie ->
                Text(
                    text = serie.seriesTitle,
                    style = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center),
                )
            }
        }
    }
}

@Preview
@Composable
fun ChartBarViewPreview() {
    SpentifyTheme {
        Card(onClick = { /*TODO*/ }) {
            ChartBarView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSYPadding.current.screenHorizontalPadding),
                data = BarData(
                    hint = "Hint",
                    total = "Total",
                    chartName = "Bar Chart",
                    chartDescription = "This is a bar chart",
                    series = listOf(
                        BarData.BarSeries(
                            color = 0xFFD0BCFF.toInt(),
                            tag = "Tag 1",
                            value = 10f,
                            seriesTitle = "Series 1"
                        ),
                        BarData.BarSeries(
                            color = 0xFFEFB8C8.toInt(),
                            tag = "Tag 2",
                            value = 20f,
                            seriesTitle = "Series 2"
                        ),
                        BarData.BarSeries(
                            color = 0xFFCCC2DC.toInt(),
                            tag = "Tag 3",
                            value = 30f,
                            seriesTitle = "Series 3"
                        )
                    )
                )
            )
        }
    }
}