package com.nestor.uikit.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nestor.uikit.SpentifyTheme
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerSkeletonBox(
    shimmerInstance: Shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f)
            .clip(MaterialTheme.shapes.small)
            .shimmer(shimmerInstance)
            .background(color = MaterialTheme.colorScheme.onBackground)
    )
}

@Composable
fun ShimmerSkeletonLine(
    widthPercentage: Float = .6f,
    shimmerInstance: Shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
) {
    Box(
        modifier = Modifier
            .height(18.dp)
            .fillMaxWidth(widthPercentage)
            .clip(MaterialTheme.shapes.extraSmall)
            .shimmer(shimmerInstance)
            .background(color = MaterialTheme.colorScheme.onBackground)
    )
}

@Composable
fun ShimmerSkeletonDoubleLine(
    modifier: Modifier = Modifier,
    widthPercentageFirstLine: Float = .6f,
    widthPercentageSecondLine: Float = .4f,
    shimmerInstance: Shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(18.dp)
                .fillMaxWidth(widthPercentageFirstLine)
                .clip(MaterialTheme.shapes.extraSmall)
                .shimmer(shimmerInstance)
                .background(color = MaterialTheme.colorScheme.onBackground)
        )
        Box(
            modifier = Modifier
                .height(18.dp)
                .fillMaxWidth(widthPercentageSecondLine)
                .clip(MaterialTheme.shapes.extraSmall)
                .shimmer(shimmerInstance)
                .background(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}

@Preview
@Composable
private fun LoadingShimmerContentPreview() {
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