package fyi.manpreet.composablememes.ui.meme.state

import fyi.manpreet.composablememes.data.model.Meme

data class MemeState(
    val meme: Meme? = null,
    val textBoxes: List<MemeTextBox> = emptyList(),
    val editorOptions: MemeEditorOptions = MemeEditorOptions(
        options = emptyList(),
        selectedOption = MemeEvent.EditorOptionsBottomBarEvent.Font
    ),
    val editorSelectionOptions: MemeEditorSelectionOptions = MemeEditorSelectionOptions(
        fonts = emptyList(),
        fontSize = 0.5f
    ),
    val isBackDialogVisible: Boolean = false,
    val shouldShowEditOptions: Boolean = false,
)