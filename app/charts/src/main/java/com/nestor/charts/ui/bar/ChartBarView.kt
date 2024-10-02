package com.nestor.charts.ui.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.charts.data.bar.BarData
import com.nestor.charts.data.bar.ChartBarHeader
import com.nestor.charts.util.Line
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun ChartBarView(modifier: Modifier = Modifier, data: BarData) {
    Card(onClick = { /*TODO*/ }) {
        Column(modifier = modifier, verticalArrangement = spacedBy(24.dp)) {
            ChartBarHeader(data = data.header)
            ChartBarContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4 / 3f),
                data = data
            )
        }
    }
}

@Composable
private fun ChartBarContent(modifier: Modifier, data: BarData) {
    Box(modifier = modifier.fillMaxSize()) {
        var labelHeight by remember { mutableStateOf(0) }
        // Background
        ChartBackground(
            modifier = Modifier.padding(bottom = with(LocalDensity.current) { labelHeight.toDp() })
        )
        // Bars
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = spacedBy(20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            val maxValue = remember(data.series) { data.series.maxOf { it.value } }
            data.series.forEach { serie ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val correspondingHeight = (serie.value / maxValue)
                    Box(modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(correspondingHeight)
                                .width(12.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(Color(serie.color))
                                .align(Alignment.BottomCenter)
                        )
                    }
                    Text(
                        text = serie.seriesTitle,
                        style = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.onGloballyPositioned { labelHeight = it.size.height }
                    )
                }
            }
        }
    }
}

@Composable
fun ChartBackground(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize()
    ) {
        repeat(4) { i ->
            Line(dashed = i != 3)
        }
    }
}

@Preview
@Composable
fun ChartBarViewPreview() {
    SpentifyTheme {
        ChartBarView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocalSYPadding.current.screenHorizontalPadding),
            data = BarData(
                header = ChartBarHeader(
                    hint = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onTertiary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("↑ 2.1% ")
                        }
                        append("vs last week")
                    },
                    total = AnnotatedString("$ 1,278"),
                    chartName = buildAnnotatedString { append("Bar Chart") },
                    chartDescription = AnnotatedString("This is a bar chart")
                ),
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