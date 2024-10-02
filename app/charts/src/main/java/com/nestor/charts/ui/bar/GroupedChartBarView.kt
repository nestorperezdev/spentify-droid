package com.nestor.charts.ui.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
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
import com.nestor.charts.data.ChartSeries
import com.nestor.charts.data.bar.ChartBarHeader
import com.nestor.charts.data.bar.grouped.GroupedBarData
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
fun GroupedChartBarView(modifier: Modifier = Modifier, data: GroupedBarData) {
    GroupedBarContent(modifier = modifier, data = data)
}

@Composable
private fun GroupedBarContent(modifier: Modifier = Modifier, data: GroupedBarData) {
    Card(
        modifier = modifier.wrapContentSize()
    ) {
        Column(modifier = Modifier.padding(LocalSYPadding.current.screenHorizontalPadding)) {
            ChartBarHeader(modifier = Modifier, data = data.header)
            GroupedChartContent(modifier = modifier.aspectRatio(4 / 3f), data = data)
            ChartBarFooter(data = data)
        }
    }
}

@Composable
fun GroupedChartContent(modifier: Modifier = Modifier, data: GroupedBarData) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .wrapContentWidth()
    ) {
        var labelHeight by remember { mutableStateOf(0) }
        // Background
        ChartBackground(
            modifier = Modifier.padding(bottom = with(LocalDensity.current) { labelHeight.toDp() })
        )
        // Bars
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = spacedBy(20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            val maxValue =
                remember(data.series) { data.series.flatMap { it.series }.maxOf { it.value } }
            data.series.forEach { series ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        horizontalArrangement = spacedBy(6.dp),
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.weight(1f)
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
                    Text(
                        text = series.seriesTitle,
                        style = MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center),
                        modifier = Modifier.onGloballyPositioned { labelHeight = it.size.height }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupedChartBarViewPreview() {
    SpentifyTheme {
        GroupedChartBarView(
            modifier = Modifier
                .wrapContentWidth()
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
                series = List(7) { idx ->
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