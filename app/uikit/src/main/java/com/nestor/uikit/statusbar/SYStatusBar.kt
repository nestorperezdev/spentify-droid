package com.nestor.uikit.statusbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nestor.uikit.button.SYTextButton
import com.nestor.uikit.loading.ShimmerSkeletonDoubleLine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SYStatusBar(
    barType: StatusBarType,
) {
    Column {
        val topPadding = when (barType.topPadding) {
            StatusTopPadding.Big -> {
                40.dp
            }

            StatusTopPadding.None -> {
                0.dp
            }
        }
        TopAppBar(
            modifier = Modifier.padding(top = topPadding),
            title = {
                TopBarTitle(barType)
            },
            navigationIcon = {
                barType.navigationIcon?.let { icon ->
                    IconButton(onClick = icon.onClick) {
                        Icon(imageVector = icon.icon, contentDescription = "")
                    }
                }
            },
            actions = {
                barType.action?.let {
                    SYTextButton(onClick = it.onClick, buttonText = it.text)
                }
            }
        )
    }
}

@Composable
fun TopBarTitle(barType: StatusBarType, startPadding: Dp = 12.dp) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        if (barType is StatusBarType.LoadingDoubleLineStatusBar) {
            ShimmerSkeletonDoubleLine()
        }
        barType.title?.let {
            Text(
                text = it.title,
                textAlign = it.alignment,
                style = it.style.getStyleFromStatusBarTextStyle(),
                modifier = Modifier
                    .padding(start = startPadding)
                    .fillMaxWidth()
            )
        }
        barType.subtitle?.let {
            Text(
                text = it.title,
                textAlign = it.alignment,
                style = it.style.getStyleFromStatusBarTextStyle(),
                modifier = Modifier
                    .padding(start = startPadding)
                    .fillMaxWidth()
            )
        }
    }
}
