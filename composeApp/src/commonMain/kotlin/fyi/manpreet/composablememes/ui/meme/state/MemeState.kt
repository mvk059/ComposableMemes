package fyi.manpreet.composablememes.ui.meme.state

import fyi.manpreet.composablememes.data.model.Meme

data class MemeState(
    val meme: Meme? = null,
    val isBackDialogVisible: Boolean = false,
    val textBoxes: List<MemeTextBox> = emptyList(),
)