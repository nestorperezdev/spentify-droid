package com.nestor.charts.ui.circle

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.nestor.charts.data.bar.BarData
import com.nestor.charts.data.circle.CircleChartData
import com.nestor.charts.data.common.ChartHeaderData
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Preview
@Composable
fun CircleChartViewPreview() {
    SpentifyTheme {
        CircleChartView(modifier = Modifier
            .fillMaxWidth()
            .padding(LocalSYPadding.current.screenHorizontalPadding),
            data = CircleChartData(
                header = ChartHeaderData(
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
                    chartName = buildAnnotatedString { append("Circle Chart") },
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