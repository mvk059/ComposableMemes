package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.graphics.Color

data class MemeEditorSelectionOptions(
    val fonts: List<Font>,
    val fontSize: Float,
    val fontColors: List<FontColor>
) {
    data class Font(
        val id: Long,
        val name: String,
        val type: String,
        val isSelected: Boolean = false,
    )

    data class FontColor(
        val id: Long,
        val color: Color,
        val isSelected: Boolean = false,
    )
}