package fyi.manpreet.composablememes.ui.meme.mapper

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
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