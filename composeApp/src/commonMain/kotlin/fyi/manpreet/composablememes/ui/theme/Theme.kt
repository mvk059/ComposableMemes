package fyi.manpreet.composablememes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MemeTheme(
    content: @Composable () -> Unit,
) {

    CompositionLocalProvider(
        LocalFixedAccentColors provides getFixedAccentColors,
        LocalTypographyFonts provides TypographyFonts(),
        LocalSpacing provides Spacing(),
        LocalGradient provides Gradients(),
    ) {

        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = Typography(),
            shapes = Shapes,
            content = content,
        )
    }
}
