package com.nestor.uikit.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer

@Preview
@Composable
fun LoadingScreenPreview() {
    SpentifyTheme {
        LoadingScreen()
    }
}


@Preview
@Composable
fun LoadingShimmerContentPreview() {
    SpentifyTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
                ShimmerSkeletonBox(shimmerInstance = shimmerInstance)
                ShimmerSkeletonLine(shimmerInstance = shimmerInstance)
                ShimmerSkeletonDoubleLine(shimmerInstance = shimmerInstance)
            }
        }
    }
}