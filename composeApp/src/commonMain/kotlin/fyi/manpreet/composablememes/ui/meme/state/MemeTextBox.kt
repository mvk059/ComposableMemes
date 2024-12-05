package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.geometry.Offset

data class MemeTextBox(
    val id: Long,
    val text: String,
    val offset: Offset,
    val isSelected: Boolean,
)