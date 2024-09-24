package com.nestor.charts.ui

import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.bar.ChartBarHeader
import com.nestor.charts.data.bar.grouped.GroupedBarData
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun GroupedChartBarView(modifier: Modifier = Modifier, data: GroupedBarData) {
    Column(modifier = modifier, verticalArrangement = spacedBy(24.dp)) {
        ChartHeader(data.header)
        GroupedChartContent(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .heightIn(max = 320.dp),
            data = data
        )
        CharBarFooter(data)
    }
}

@Composable
fun GroupedChartContent(modifier: Modifier = Modifier, data: GroupedBarData) {
    Box(modifier = modifier.fillMaxSize()) {
        // Background
        ChartBackground()
        // Bars
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            val maxValue =
                remember(data.series) { data.series.flatMap { it.series }.maxOf { it.value } }
            data.series.forEach { series ->
                Row(
                    horizontalArrangement = spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    series.series.forEach { serie ->
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

@Composable
private fun CharBarFooter(data: GroupedBarData) {
    Row {
        data.groupSeriesByTagAndColor().forEach { (tagColor: Pair<String, Int>, _) ->
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

@Preview
@Composable
fun GroupedChartBarViewPreview() {
    SpentifyTheme {
        Card(onClick = { /*TODO*/ }) {
            GroupedChartBarView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalSYPadding.current.screenHorizontalPadding),
                data = GroupedBarData(
                    header = ChartBarHeader(
                        hint = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("↓ 3.5% ")
                            }
                            append("vs last week")
                        },
                        total = AnnotatedString("$ 1,278"),
                        chartName = AnnotatedString("Bar Chart"),
                        chartDescription = AnnotatedString("This is a bar chart")
                    ),
                    series = List(3) { idx ->
                        GroupedBarData.GroupedSeries(
                            seriesTitle = "Series $idx",
                            series = listOf(
                                ChartSeries(
                                    color = 0xFFD0BCFF.toInt(),
                                    tag = "Tag 1",
                                    value = 1f * idx + 1,
                                ),
                                ChartSeries(
                                    color = 0xFFCCC2DC.toInt(),
                                    tag = "Tag 2",
                                    value = 1.5f * idx + 1,
                                ),
                                ChartSeries(
                                    color = 0xFFEFB8C8.toInt(),
                                    tag = "Tag 3",
                                    value = 1.2f * idx + 1,
                                ),
                            )
                        )
                    },
                )
            )
        }
    }
}