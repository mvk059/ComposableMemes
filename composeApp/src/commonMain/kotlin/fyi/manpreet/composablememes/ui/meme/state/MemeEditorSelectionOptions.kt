package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.graphics.Color

data class MemeEditorSelectionOptions(
    val font: Fonts,
    val fontSize: Float,
    val fontColors: List<FontColor>
) {

    data class Fonts(
        val fonts: List<Font>,
        val example: String,
    ) {

        data class Font(
            val id: FontFamilyType,
            val name: String,
            val isSelected: Boolean = false,
        )
    }

    data class FontColor(
        val id: Long,
        val color: Color,
        val isSelected: Boolean = false,
    )
}

enum class FontFamilyType {
    AntonSC,
    DancingScript,
    Jaro,
    Lobster,
    Manrope,
    OpenSans,
    Roboto,
    RubrikDoodleShadow,
}