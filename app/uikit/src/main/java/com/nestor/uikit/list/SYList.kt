package com.nestor.uikit.list

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SYList(
    modifier: Modifier = Modifier,
    items: List<SYListItemData>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(20.dp)
    ) {
        items.forEach { item ->
            SYListItem(item = item)
        }
    }
    /*LazyColumn(
        modifier = modifier,
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = spacedBy(20.dp),
        userScrollEnabled = false
    ) {
        items(items.size, key = { items[it].key }) { index ->
            SYListItem(item = items[index])
        }
    }*/
}