package com.nestor.charts.ui.bar

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nestor.charts.data.bar.ChartBarHeader

@Composable
internal fun ChartBarHeader(
    modifier: Modifier = Modifier,
    data: ChartBarHeader
) {
    Column(verticalArrangement = spacedBy(12.dp), modifier = modifier) {
        Text(text = data.chartName, style = MaterialTheme.typography.titleLarge)
        Text(text = data.chartDescription, style = MaterialTheme.typography.titleSmall)
        if (data.hint != null || data.total != null) {
            Column(verticalArrangement = spacedBy(8.dp)) {
                data.total?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 32.sp)
                    )
                }
                data.hint?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = LocalContentColor.current.copy(alpha = 0.8f)
                        )
                    )
                }
            }
        }
    }
}