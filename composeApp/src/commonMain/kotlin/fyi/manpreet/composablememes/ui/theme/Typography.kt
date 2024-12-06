package fyi.manpreet.composablememes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import composablememes.composeapp.generated.resources.Manrope_Bold
import composablememes.composeapp.generated.resources.Manrope_Medium
import composablememes.composeapp.generated.resources.Manrope_Regular
import composablememes.composeapp.generated.resources.Manrope_SemiBold
import composablememes.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun Typography(): Typography {
    val manrope = FontFamily(
        Font(Res.font.Manrope_Regular, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.Manrope_Medium, FontWeight.Medium, FontStyle.Normal),
        Font(Res.font.Manrope_SemiBold, FontWeight.SemiBold, FontStyle.Normal),
        Font(Res.font.Manrope_Bold, FontWeight.Bold, FontStyle.Normal),
    )

    return Typography(
        titleLarge = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 28.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            lineHeight = 30.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 22.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 28.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 22.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp
        ),
        labelMedium = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelSmall = TextStyle(
            fontFamily = manrope,
            fontWeight = FontWeight.Light,
            fontSize = 10.sp,
            lineHeight = 14.sp,
            letterSpacing = 0.2.sp,
        )
    )
}
