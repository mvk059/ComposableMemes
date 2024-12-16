package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle

data class MemeTextBox(
    val id: Long,
    val text: String,
    val offset: Offset,
    val relativePosition: RelativePosition,
    val isSelected: Boolean,
    val isEditable: Boolean,
    val textStyle: TextStyle = TextStyle(),
    val fontFamilyType: FontFamilyType,
) {

    data class RelativePosition(
        val percentX: Float,
        val percentY: Float
    )
}
