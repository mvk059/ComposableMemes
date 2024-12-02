package fyi.manpreet.composablememes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MemeTheme(
    fixedAccentColors: FixedAccentColors = getFixedAccentColors,
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalFixedAccentColors provides fixedAccentColors,
        LocalSpacing provides Spacing(),
    ) {

        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = Typography(),
            shapes = Shapes,
            content = content,
        )
    }
}
