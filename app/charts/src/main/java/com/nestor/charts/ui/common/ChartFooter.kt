package com.nestor.charts.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nestor.charts.data.common.GroupableByTagAndColor

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ChartFooter(data: GroupableByTagAndColor) {
    Row {
        data.groupByTagAndColor().forEach { (tagColor: Pair<String, Int>, _) ->
            Row {
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