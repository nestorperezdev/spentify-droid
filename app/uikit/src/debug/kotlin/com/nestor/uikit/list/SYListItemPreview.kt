package com.nestor.uikit.list

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.R
import com.nestor.uikit.SpentifyTheme

private const val TAG = "SYListItemPreview"

@Preview(showBackground = true)
@Composable
fun SYListItemPreview() {
    SpentifyTheme {
        SYListItem(
            modifier = Modifier.padding(16.dp),
            item = SYListItemData(
                label = "Label",
                leadingIcon = SYListItemData.SYListItemIcon(
                    icon = painterResource(id = R.drawable.baseline_home_24),
                    tint = MaterialTheme.colorScheme.primary
                ),
                trailingIcon = SYListItemData.SYListItemIcon(
                    icon = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    foregroundTint = null
                ),
                subtitle = "Subtitle",
                contextualActions = SYListItemData.ContextualActionContainer(
                    primary = SYListItemData.ContextualAction(
                        icon = SYListItemData.SYListItemIcon(
                            icon = painterResource(id = R.drawable.baseline_delete_24),
                            tint = MaterialTheme.colorScheme.error,
                            foregroundTint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
                        ),
                        action = {
                            Log.i(TAG, "SYListItemPreview: Delete action")
                        }
                    ),
                    secondary = SYListItemData.ContextualAction(
                        icon = SYListItemData.SYListItemIcon(
                            icon = painterResource(id = R.drawable.baseline_warning_24),
                            tint = MaterialTheme.colorScheme.primary,
                            foregroundTint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        ),
                        action = {
                            Log.i(TAG, "SYListItemPreview: info action")
                        }
                    )
                )
            )
        )
    }
}