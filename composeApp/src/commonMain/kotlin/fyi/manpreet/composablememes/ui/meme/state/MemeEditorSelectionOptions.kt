package fyi.manpreet.composablememes.ui.meme.state

data class MemeEditorSelectionOptions(
    val fonts: List<Font>,
) {
    data class Font(
        val id: Long,
        val name: String,
        val type: String,
        val isSelected: Boolean = false,
    )
}