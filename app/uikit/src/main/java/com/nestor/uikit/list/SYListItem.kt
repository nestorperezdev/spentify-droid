package com.nestor.uikit.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.R
import com.nestor.uikit.SpentifyTheme

@Composable
fun SYListItem(
    modifier: Modifier = Modifier,
    label: String,
    leadingIconTint: Color = MaterialTheme.colorScheme.primary,
    leadingForegroundTint: Color? = leadingIconTint.copy(alpha = 0.05f),
    leadingIcon: Painter? = null,
    trailingIconTint: Color = MaterialTheme.colorScheme.onBackground,
    trailingForegroundTint: Color? = null,
    trailingIcon: Painter? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFC9C9C9),
                shape = MaterialTheme.shapes.large
            )
            .clickable {  }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = CenterVertically
        ) {
            Spacer(modifier = Modifier.width(24.dp))
            leadingIcon?.let { painter ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(42.dp)
                        .background(leadingForegroundTint ?: Color.Transparent),
                    contentAlignment = Center
                ) {
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        tint = leadingIconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.W300
            )
            Spacer(modifier = Modifier.weight(1f))
            trailingIcon?.let { painter ->
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(42.dp)
                        .background(trailingForegroundTint ?: Color.Transparent),
                    contentAlignment = Center
                ) {
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        tint = trailingIconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SYListItemPreview() {
    SpentifyTheme {
        SYListItem(
            modifier = Modifier.padding(16.dp),
            label = "Label",
            leadingIcon = painterResource(id = R.drawable.baseline_home_24),
            trailingIcon = painterResource(id = R.drawable.baseline_arrow_forward_ios_24)
        )
    }
}