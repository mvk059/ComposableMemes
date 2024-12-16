package fyi.manpreet.composablememes.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import com.composables.core.SheetDetent
import fyi.manpreet.composablememes.ui.meme.state.MemeTextBox.RelativePosition

val Peek = SheetDetent(identifier = "peek") { containerHeight, sheetHeight ->
    containerHeight * 0.5f
}

fun IntSize.middle(): Offset {
    return Offset(width / 4f, height / 2f)
}

fun IntSize.relativeMiddle(contentOffset: Offset): RelativePosition {
    val middlePoint = this.middle()
    return RelativePosition(
        percentX = middlePoint.x / this.width,
        percentY = middlePoint.y / this.height
    )
}

fun RelativePosition.toOffset(size: Size, contentOffset: Offset): Offset {
    return Offset(
        x = (percentX * size.width) + contentOffset.x,
        y = (percentY * size.height) + contentOffset.y
    )
}

fun Offset.toRelativePosition(size: Size, contentOffset: Offset): RelativePosition {
    return RelativePosition(
        percentX = (x - contentOffset.x) / size.width,
        percentY = (y - contentOffset.y) / size.height
    )
}