package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle

data class MemeTextBox(
    val id: Long,
    val text: String,
    val offset: Offset,
    val isSelected: Boolean,
    val textStyle: TextStyle = TextStyle(),
)
