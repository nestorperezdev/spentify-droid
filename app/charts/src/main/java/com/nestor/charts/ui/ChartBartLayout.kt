package com.nestor.charts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.nestor.charts.data.bar.ChartBarHeader
import com.nestor.uikit.SpentifyTheme
import com.nestor.uikit.theme.spacing.LocalSYPadding

@Composable
internal fun ChartBarLayout(
    modifier: Modifier,
    header: @Composable (Modifier) -> Unit,
    background: @Composable (Modifier) -> Unit,
    bars: List<@Composable () -> Unit>
) {
    Card(
        onClick = { /*TODO*/ }, modifier = modifier
            .fillMaxSize()
            .aspectRatio(3 / 4f)
    ) {
        var containerSize by remember { mutableStateOf(0) }
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(LocalSYPadding.current.screenHorizontalPadding)
                .onGloballyPositioned { containerSize = it.size.width }
        ) {
            val (background, header) = createRefs()
            header(Modifier.constrainAs(header) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            })
            background(
                Modifier
                    .constrainAs(background) {
                        top.linkTo(header.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )
            if (bars.isEmpty()) {
                val noData = createRef()
                Text(
                    text = "No data to display",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.constrainAs(noData) {
                        top.linkTo(background.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                )
            } else {
                val refs = bars.map { createRef() }
                bars.forEachIndexed { idx, bar ->
                    val ref = refs[idx]
                    val position = getBarPosition(
                        totalWidth = containerSize,
                        numberOfBars = bars.size,
                        currentItemIndex = idx
                    )
                    Box(
                        Modifier
                            .constrainAs(ref) {
                                top.linkTo(background.top)
                                bottom.linkTo(background.bottom)
                                height = Dimension.fillToConstraints
                                refs.getOrNull(idx - 1)?.let { p ->
                                    start.linkTo(p.end, position.dp)
                                } ?: run {
                                    start.linkTo(background.start, position.dp)
                                }
                                width = Dimension.wrapContent
                            }
                    ) {
                        bar()
                    }
                }
            }
        }
    }
}

private fun getBarPosition(totalWidth: Int, numberOfBars: Int, currentItemIndex: Int): Int {
    return totalWidth / (numberOfBars + 1) * (currentItemIndex + 1)
}

@Preview
@Composable
fun ChartBarLayoutPreview() {
    SpentifyTheme {
        ChartBarLayout(
            Modifier.fillMaxSize(),
            background = { ChartBackground(modifier = it) },
            bars = listOf(
                {
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .fillMaxHeight(1f)
                            .background(Color.Red)
                    )
                },
                {
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .fillMaxHeight(1f)
                            .background(Color.Blue)
                    )
                }
            ),
            header = {
                ChartHeader(
                    modifier = it, data = ChartBarHeader(
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
                    )
                )
            }
        )
    }
}