package fyi.manpreet.composablememes.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.composables.core.SheetDetent

val Peek = SheetDetent(identifier = "peek") { containerHeight, sheetHeight ->
    containerHeight * 0.5f
}

fun IntSize.middle(): Offset {
    return Offset(width / 4f, height / 2f)
}