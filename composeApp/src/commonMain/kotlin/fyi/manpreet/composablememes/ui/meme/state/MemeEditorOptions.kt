package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.resources.DrawableResource

data class MemeEditorOptions(
    val editorSize: IntSize,
    var imageContentSize: Size,
    var imageContentOffset: Offset,
    val options: List<Options>,
    val selectedOption: MemeEvent.EditorOptionsBottomBarEvent
) {
    data class Options(
        val type: MemeEvent.EditorOptionsBottomBarEvent,
        val icon: DrawableResource,
    )
}
