package fyi.manpreet.composablememes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val default: Dp = 0.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val largeXL: Dp = 32.dp,
    val large2XL: Dp = 48.dp,
    val large3XL: Dp = 64.dp,
    val fabContainerWidth: Dp = 56.dp,
    val fabContainerHeight: Dp = 56.dp,
    val listItemWidth: Dp = 176.dp,
    val listItemHeight: Dp = 176.dp,
    val sortFilterSize: Dp = 120.dp,
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

val MaterialTheme.spacing: Spacing
    @Composable @ReadOnlyComposable get() = LocalSpacing.current