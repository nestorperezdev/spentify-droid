package com.nestor.uikit.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SYList(
    modifier: Modifier = Modifier,
    items: List<SYListItemData>,
    onItemClick: (item: SYListItemData) -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(20.dp)
    ) {
        items.forEach { item ->
            SYListItem(item = item, modifier = Modifier.clickable { onItemClick(item) })
        }
    }
}