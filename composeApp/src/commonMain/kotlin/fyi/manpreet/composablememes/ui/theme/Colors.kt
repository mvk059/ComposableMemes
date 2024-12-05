package fyi.manpreet.composablememes.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF65558F)
val PrimaryContainer = Color(0xFFEADDFF)
val OnPrimaryFixed = Color(0xFF21005D)
val WhiteTertiary = Color(0xFFFFFFFF)
val Secondary = Color(0xFFCCC2DC)
val SecondaryFixedDim = Color(0xFFCCC2DC)
val Tertiary = Color(0xFF79747E)
val DarkBackground = Color(0xFF0F0D13)
val DarkOnBackground = Color(0xFF79747E)
val DarkSurface = Color(0xFF1D1B20)
val DarkOnSurface = Color(0xFFE6E0E9)
val DarkTextColor = Color(0xFFE0E0E0)
val SurfaceContainerLowest = Color(0xFF0F0D13)
val SurfaceContainerLow = Color(0xFF1D1B20)
val SurfaceContainer = Color(0xFF211F26)
val SurfaceContainerHigh = Color(0xFF2B2930)
val Outline = Color(0xFF79747E)
val Error = Color(0xFFB3261E)

val PurpleLight2 = Color(0xFFE0D0FA)
val PurpleMedium1 = Color(0xFFD0BCFE)
val PurpleMedium2 = Color(0xFFAD90F1)

internal val DarkColorScheme = darkColorScheme(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    secondary = Secondary,
    onSecondary = DarkTextColor,
    tertiary = Tertiary,
    onTertiary = WhiteTertiary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    outline = Outline,
    error = Error,
)
