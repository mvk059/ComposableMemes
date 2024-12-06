package fyi.manpreet.composablememes.ui.meme.state

import org.jetbrains.compose.resources.DrawableResource

data class MemeEditorOptions(
    val options: List<Options>,
) {
    data class Options(
        val type: MemeEvent.EditorOptionsBottomBarEvent,
        val icon: DrawableResource,
        val isSelected: Boolean,
    )
}
