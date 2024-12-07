package fyi.manpreet.composablememes.ui.meme.mapper

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import fyi.manpreet.composablememes.ui.meme.state.FontFamilyType
import fyi.manpreet.composablememes.ui.theme.typographyFonts
import fyi.manpreet.composablememes.util.MemeConstants

typealias SliderValue = Float

//@Composable
fun SliderValue.sliderValueToFontSize(): TextUnit {
    val minFontSize = MemeConstants.MIN_FONT_SIZE
    val maxFontSize = MemeConstants.MAX_FONT_SIZE

    // Linear interpolation
    val mappedValue = minFontSize + (maxFontSize - minFontSize) * this
    return mappedValue.sp
}

fun invertColor(color: Color): Color {
    return Color(
        red = 1f - color.red,
        green = 1f - color.green,
        blue = 1f - color.blue,
        alpha = color.alpha
    )
}

@Composable
fun FontFamilyType.toFontFamily(): FontFamily {
    return when (this) {
        FontFamilyType.AntonSC -> MaterialTheme.typographyFonts.antonSC
        FontFamilyType.DancingScript -> MaterialTheme.typographyFonts.dancingScript
        FontFamilyType.Jaro -> MaterialTheme.typographyFonts.jaro
        FontFamilyType.Lobster -> MaterialTheme.typographyFonts.lobster
        FontFamilyType.Manrope -> MaterialTheme.typographyFonts.manrope
//        FontFamilyType.NotoSansSymbols -> MaterialTheme.typographyFonts.notoSansSymbols
        FontFamilyType.OpenSans -> MaterialTheme.typographyFonts.openSans
        FontFamilyType.Roboto -> MaterialTheme.typographyFonts.roboto
        FontFamilyType.RubrikDoodleShadow -> MaterialTheme.typographyFonts.rubrikDoodleShadow
    }
}
