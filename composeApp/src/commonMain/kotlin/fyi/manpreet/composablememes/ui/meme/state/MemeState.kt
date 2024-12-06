package fyi.manpreet.composablememes.ui.meme.state

import fyi.manpreet.composablememes.data.model.Meme

data class MemeState(
    val meme: Meme? = null,
    val textBoxes: List<MemeTextBox> = emptyList(),
    val editorOptions: MemeEditorOptions = MemeEditorOptions(options = emptyList()),
    val editorSelectionOptions: MemeEditorSelectionOptions = MemeEditorSelectionOptions(fonts = emptyList()),
    val isBackDialogVisible: Boolean = false,
    val shouldShowEditOptions: Boolean = false,
)