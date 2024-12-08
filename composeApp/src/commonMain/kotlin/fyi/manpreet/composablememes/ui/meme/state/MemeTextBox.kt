package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle

data class MemeTextBox(
    val id: Long,
    val text: String,
    val offset: Offset,
    val isSelected: Boolean,
    val isEditable: Boolean,
    val textStyle: TextStyle = TextStyle(),
    val fontFamilyType: FontFamilyType,
)
