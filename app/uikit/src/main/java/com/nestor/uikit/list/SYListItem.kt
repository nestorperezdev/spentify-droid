package com.nestor.uikit.list

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.R
import com.nestor.uikit.SpentifyTheme
import kotlin.math.absoluteValue

private const val TAG = "SYListItem"

enum class DragDirection {
    LEFT,
    RIGHT
}

@Composable
fun SYListItem(
    modifier: Modifier = Modifier,
    item: SYListItemData,
    onDragged: (dragDirection: DragDirection) -> Float = { 0f }
) {
    val density = LocalDensity.current
    var width by remember { mutableIntStateOf(0) }
    val hasSecondaryAction = item.contextualActions?.secondary != null
    var draggedValue by remember { mutableFloatStateOf(0f) }
    val draggedDp = remember(draggedValue) { with(density) { draggedValue.toDp() } }
    val dragDirection = remember(draggedValue) { if (draggedValue > 0) DragDirection.RIGHT else DragDirection.LEFT }
    val draggableState = rememberDraggableState {
        val value = it + draggedValue
        if (hasSecondaryAction || value <= 0) {
            if (value.absoluteValue < width * 0.45f) {
                draggedValue += it
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(66.dp)
            .onSizeChanged { width = it.width }
            .offset(x = draggedDp)
            .draggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                enabled = item.contextualActions != null,
                onDragStopped = {
                    if (draggedValue.absoluteValue > width * 0.35f) {
                        draggedValue = onDragged(dragDirection)
                        if (dragDirection == DragDirection.LEFT) {
                            item.contextualActions?.primary?.action?.invoke()
                        } else if (dragDirection == DragDirection.RIGHT) {
                            item.contextualActions?.secondary?.action?.invoke()
                        }
                    } else {
                        draggedValue = 0f
                    }
                },
            )
            .border(
                width = 1.dp,
                color = Color(0xFFC9C9C9),
                shape = MaterialTheme.shapes.large
            )
    ) {
        item.contextualActions?.let { actions ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = -draggedDp)
                    .clip(MaterialTheme.shapes.large),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                actions.secondary?.let { action ->
                    SYListItemContextualAction(
                        action = action,
                        modifier = Modifier.weight(1f),
                        draggedDirection = dragDirection
                    )
                }
                SYListItemContextualAction(
                    action = actions.primary,
                    modifier = Modifier.weight(1f),
                    draggedDirection = dragDirection
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surface),
            verticalAlignment = CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(24.dp))
            item.leadingIcon?.let { icon ->
                SYItemIcon(icon = icon)
                Spacer(modifier = Modifier.width(24.dp))
            }
            Column {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.W300
                )
                item.subtitle?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            item.trailingIcon?.let { icon ->
                SYItemIcon(icon = icon)
                Spacer(modifier = Modifier.width(24.dp))
            }
        }
    }
}

@Composable
fun SYListItemContextualAction(
    modifier: Modifier = Modifier,
    action: SYListItemData.ContextualAction,
    draggedDirection: DragDirection
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .background(color = action.icon.foregroundTint ?: Color.Transparent),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (draggedDirection == DragDirection.LEFT) {
            Spacer(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .width(1.dp))
        }
        Icon(
            painter = action.icon.icon,
            contentDescription = null,
            tint = action.icon.tint,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .size(36.dp)
        )
        if (draggedDirection == DragDirection.RIGHT) {
            Spacer(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .width(1.dp))
        }
    }
}

@Composable
fun SYItemIcon(
    modifier: Modifier = Modifier,
    icon: SYListItemData.SYListItemIcon,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(42.dp)
            .background(icon.foregroundTint ?: Color.Transparent),
        contentAlignment = Center
    ) {
        Icon(
            painter = icon.icon,
            contentDescription = null,
            tint = icon.tint,
            modifier = Modifier.size(22.dp)
        )
    }
}

fun LazyListState.isScrolledToEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@Preview(showBackground = true)
@Composable
private fun SYListItemPreview() {
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