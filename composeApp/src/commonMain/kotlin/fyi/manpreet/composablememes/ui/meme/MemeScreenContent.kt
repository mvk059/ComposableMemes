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
import fyi.manpreet.composablememes.ui.meme.components.dialog.BackConfirmationDialog
import fyi.manpreet.composablememes.ui.meme.components.meme.MemeImage
import fyi.manpreet.composablememes.ui.meme.components.topbar.MemeTopBar
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.MemeTextBox

@Composable
fun MemeScreenContent(
    modifier: Modifier = Modifier,
    meme: Meme?,
    textBoxes: List<MemeTextBox>,
    isBackDialogVisible: Boolean,
    onBackConfirmClickTopBar: (MemeEvent.TopBarEvent) -> Unit,
    onCancelClickDialog: (MemeEvent.TopBarEvent) -> Unit,
    onBackClickDialog: () -> Unit,
    onAddTextBottomBar: (MemeEvent.EditorEvent) -> Unit,
    onPositionUpdateEditor: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxClickEditor: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxCloseClickEditor: (MemeEvent.EditorEvent) -> Unit,
    onDeselectClickEditor: (MemeEvent.EditorEvent) -> Unit,
) {

    if (meme == null) return

    Scaffold(
        topBar = {
            MemeTopBar(
                onBackClick = onBackConfirmClickTopBar,
            )
        },
        bottomBar = {
            MemeBottomBar(
                onAddText = onAddTextBottomBar,
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MemeImage(
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
                meme = meme,
                textBoxes = textBoxes,
                onPositionUpdate = onPositionUpdateEditor,
                onTextBoxClick = onTextBoxClickEditor,
                onTextBoxCloseClick = onTextBoxCloseClickEditor,
                onDeselectClick = onDeselectClickEditor,
            )
        }


        if (isBackDialogVisible) {
            BackConfirmationDialog(
                onBack = onBackClickDialog,
                onCancel = onCancelClickDialog
            )
        }
    }
}
