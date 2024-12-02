package fyi.manpreet.composablememes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class FixedAccentColors(
    val onPrimaryFixed: Color,
    val secondaryFixedDim: Color,
)

val getFixedAccentColors: FixedAccentColors = FixedAccentColors(
    onPrimaryFixed = OnPrimaryFixed,
    secondaryFixedDim = SecondaryFixedDim,
)

val LocalFixedAccentColors = staticCompositionLocalOf {
    FixedAccentColors(
        onPrimaryFixed = OnPrimaryFixed,
        secondaryFixedDim = SecondaryFixedDim,
    )
}

val MaterialTheme.fixedAccentColors: FixedAccentColors
    @Composable @ReadOnlyComposable get() = LocalFixedAccentColors.current

/*
For Reference
data class FixedAccentColors(
    val primaryFixed: Color,
    val onPrimaryFixed: Color,
    val secondaryFixed: Color,
    val onSecondaryFixed: Color,
    val tertiaryFixed: Color,
    val onTertiaryFixed: Color,
    val primaryFixedDim: Color,
    val secondaryFixedDim: Color,
    val tertiaryFixedDim: Color,
)

val getFixedAccentColors: FixedAccentColors = FixedAccentColors(
    primaryFixed = colorScheme.primaryContainer,
    onPrimaryFixed = OnPrimaryFixed,
    secondaryFixed = colorScheme.secondaryContainer,
    onSecondaryFixed = colorScheme.onSecondaryContainer,
    tertiaryFixed = colorScheme.tertiaryContainer,
    onTertiaryFixed = colorScheme.onTertiaryContainer,
    primaryFixedDim = colorScheme.primary,
    secondaryFixedDim = SecondaryFixedDim,
    tertiaryFixedDim = colorScheme.tertiary
)
 */