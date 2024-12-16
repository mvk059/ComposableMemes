package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import fyi.manpreet.composablememes.data.model.Meme

data class MemeState(
    val meme: Meme? = null,
    val textBoxes: List<MemeTextBox> = emptyList(),
    val editorOptions: MemeEditorOptions = MemeEditorOptions(
        editorSize = IntSize.Zero,
        imageContentSize = Size.Zero,
        imageContentOffset = Offset.Zero,
        options = emptyList(),
        selectedOption = MemeEvent.EditorOptionsBottomBarEvent.Font
    ),
    val editorSelectionOptions: MemeEditorSelectionOptions = MemeEditorSelectionOptions(
        font = MemeEditorSelectionOptions.Fonts(
            fonts = emptyList(),
            example = ""
        ),
        fontSize = 0.5f,
        fontColors = emptyList()
    ),
    val shareOptions: List<ShareOption> = emptyList(),
    val isBackDialogVisible: Boolean = false,
    val shouldShowEditOptions: Boolean = false,
)