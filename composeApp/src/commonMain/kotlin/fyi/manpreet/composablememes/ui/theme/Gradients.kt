package fyi.manpreet.composablememes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class Gradients(
    val default: Brush = GradientDefault,
    val pressed: Brush = GradientPressed,
    val favorite: Brush = GradientFavorite,
) {

    private companion object {

        val GradientDefault = Brush.linearGradient(
            colors = listOf(PrimaryContainer, PurpleMedium1)
        )

        val GradientPressed = Brush.linearGradient(
            colors = listOf(PurpleLight2, PurpleMedium2)
        )

        val GradientFavorite = Brush.linearGradient(
            colorStops = arrayOf(
                0.0f to Color.Transparent,
                0.8f to Color.Transparent,
                1.0f to Color(0xFF332b28)
            ),
        )
    }
}

val LocalGradient = staticCompositionLocalOf { Gradients() }

val MaterialTheme.gradient: Gradients
    @Composable @ReadOnlyComposable get() = LocalGradient.current