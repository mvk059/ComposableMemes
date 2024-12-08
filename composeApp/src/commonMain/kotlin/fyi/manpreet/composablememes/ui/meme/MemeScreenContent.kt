package fyi.manpreet.composablememes.ui.meme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.meme.components.bottombar.MemeBottomBar
import fyi.manpreet.composablememes.ui.meme.components.bottombar.MemeBottomBarEditOptions
import fyi.manpreet.composablememes.ui.meme.components.dialog.BackConfirmationDialog
import fyi.manpreet.composablememes.ui.meme.components.meme.MemeImage
import fyi.manpreet.composablememes.ui.meme.components.topbar.MemeTopBar
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorSelectionOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.MemeTextBox

@Composable
fun MemeScreenContent(
    modifier: Modifier = Modifier,
    meme: Meme?,
    textBoxes: List<MemeTextBox>,
    editorOptionsBottomBar: MemeEditorOptions,
    editorSelectionOptionsBottomBar: MemeEditorSelectionOptions,
    isBackDialogVisible: Boolean,
    shouldShowEditOptions: Boolean,
    onBackConfirmClickTopBar: (MemeEvent.TopBarEvent) -> Unit,
    onCancelClickDialog: (MemeEvent.TopBarEvent) -> Unit,
    onBackClickDialog: () -> Unit,
    onAddTextBottomBar: (MemeEvent.EditorEvent) -> Unit,
    onPositionUpdateEditor: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxClickEditor: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxCloseClickEditor: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxTextChangeEditor: (MemeEvent.EditorEvent) -> Unit,
    onDeselectClickEditor: (MemeEvent.EditorEvent) -> Unit,
    onEditorOptionsItemClickBottomBar: (MemeEvent.EditorOptionsBottomBarEvent) -> Unit,
    onFontItemSelectBottomBar: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit,
    onFontSizeChangeBottomBar: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit,
    onFontColorSelectBottomBar: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit,
) {

    if (meme == null) return

    Scaffold(
        topBar = {
            MemeTopBar(
                onBackClick = onBackConfirmClickTopBar,
            )
        },
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MemeImage(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                meme = meme,
                textBoxes = textBoxes,
                onPositionUpdate = onPositionUpdateEditor,
                onTextBoxClick = onTextBoxClickEditor,
                onTextBoxCloseClick = onTextBoxCloseClickEditor,
                onTextBoxTextChange = onTextBoxTextChangeEditor,
                onDeselectClick = onDeselectClickEditor,
            )

            if (shouldShowEditOptions) {
                MemeBottomBarEditOptions(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    editorOptions = editorOptionsBottomBar,
                    editorSelectionOptions = editorSelectionOptionsBottomBar,
                    onEditorOptionsItemClick = onEditorOptionsItemClickBottomBar,
                    onFontItemSelect = onFontItemSelectBottomBar,
                    onFontSizeChange = onFontSizeChangeBottomBar,
                    onFontColorItemSelect = onFontColorSelectBottomBar,
                )
            } else {
                MemeBottomBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    onAddText = onAddTextBottomBar,
                )
            }
        }

        if (isBackDialogVisible) {
            BackConfirmationDialog(
                onBack = onBackClickDialog,
                onCancel = onCancelClickDialog
            )
        }
    }
}
