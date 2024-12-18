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

    companion object {

        fun init() = MemeEditorSelectionOptions(
            font = Fonts(
                fonts = emptyList(),
                example = ""
            ),
            fontSize = DEFAULT_FONT_SIZE,
            fontColors = emptyList()
        )

        const val DEFAULT_FONT_SIZE = 0.5f
    }
}

fun MemeEditorSelectionOptions.clearAllSelections() = copy(
    font = font.copy(fonts = font.fonts.map { it.copy(isSelected = false) }),
    fontSize = MemeEditorSelectionOptions.DEFAULT_FONT_SIZE,
    fontColors = fontColors.map { it.copy(isSelected = false) }
)

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