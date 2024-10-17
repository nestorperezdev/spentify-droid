package com.nestor.charts.data.common

import androidx.compose.ui.text.AnnotatedString

open class ChartHeaderData(
    val chartName: AnnotatedString,
    val chartDescription: AnnotatedString,
    val hint: AnnotatedString? = null,
    val total: AnnotatedString? = null,
)
