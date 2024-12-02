package fyi.manpreet.composablememes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush

data class Gradients(
    val default: Brush = GradientDefault,
    val pressed: Brush = GradientPressed,
) {

    private companion object {

        val GradientDefault = Brush.linearGradient(
            colors = listOf(PrimaryContainer, PurpleMedium1)
        )

        val GradientPressed = Brush.linearGradient(
            colors = listOf(PurpleLight2, PurpleMedium2)
        )
    }
}

val LocalGradient = staticCompositionLocalOf { Gradients() }

val MaterialTheme.gradient: Gradients
    @Composable @ReadOnlyComposable get() = LocalGradient.current