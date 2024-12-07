package fyi.manpreet.composablememes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import composablememes.composeapp.generated.resources.AntonSC_Regular
import composablememes.composeapp.generated.resources.DancingScript_Regular
import composablememes.composeapp.generated.resources.Jaro_60pt_Regular
import composablememes.composeapp.generated.resources.Lobster_Regular
import composablememes.composeapp.generated.resources.Manrope_Regular
import composablememes.composeapp.generated.resources.OpenSans_Regular
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.Roboto_Regular
import composablememes.composeapp.generated.resources.RubikDoodleShadow_Regular
import org.jetbrains.compose.resources.Font

@Immutable
data class TypographyFonts(
    val antonSC: FontFamily,
    val dancingScript: FontFamily,
    val jaro: FontFamily,
    val lobster: FontFamily,
    val manrope: FontFamily,
    val openSans: FontFamily,
    val roboto: FontFamily,
    val rubrikDoodleShadow: FontFamily,
)

@Composable
fun TypographyFonts(): TypographyFonts {
    val antonSC = FontFamily(
        Font(Res.font.AntonSC_Regular, FontWeight.Normal, FontStyle.Normal),
    )
    val dancingScript = FontFamily(
        Font(Res.font.DancingScript_Regular, FontWeight.Normal, FontStyle.Normal),
    )
    val jaro = FontFamily(
        Font(Res.font.Jaro_60pt_Regular, FontWeight.Normal, FontStyle.Normal),
    )
    val lobster = FontFamily(
        Font(Res.font.Lobster_Regular, FontWeight.Normal, FontStyle.Normal),
    )
    val manrope = FontFamily(
        Font(Res.font.Manrope_Regular, FontWeight.Normal, FontStyle.Normal),
    )
    val openSans = FontFamily(
        Font(Res.font.OpenSans_Regular, FontWeight.Normal, FontStyle.Normal),
    )
    val roboto = FontFamily(
        Font(Res.font.Roboto_Regular, FontWeight.Normal, FontStyle.Normal),
    )
    val rubrikDoodleShadow = FontFamily(
        Font(Res.font.RubikDoodleShadow_Regular, FontWeight.Normal, FontStyle.Normal),
    )

    return TypographyFonts(
        antonSC = antonSC,
        dancingScript = dancingScript,
        jaro = jaro,
        lobster = lobster,
        manrope = manrope,
        openSans = openSans,
        roboto = roboto,
        rubrikDoodleShadow = rubrikDoodleShadow,
    )
}

val LocalTypographyFonts = staticCompositionLocalOf {
    TypographyFonts(
        antonSC = FontFamily.Default,
        dancingScript = FontFamily.Default,
        jaro = FontFamily.Default,
        lobster = FontFamily.Default,
        manrope = FontFamily.Default,
        openSans = FontFamily.Default,
        roboto = FontFamily.Default,
        rubrikDoodleShadow = FontFamily.Default,
    )
}

val MaterialTheme.typographyFonts: TypographyFonts
    @Composable @ReadOnlyComposable get() = LocalTypographyFonts.current
