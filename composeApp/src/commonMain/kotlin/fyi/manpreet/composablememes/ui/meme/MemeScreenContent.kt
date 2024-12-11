package fyi.manpreet.composablememes.ui.meme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.rememberGraphicsLayer
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
import kotlinx.coroutines.launch

@Composable
fun MemeScreenContent(
    meme: Meme?,
    textBoxes: List<MemeTextBox>,
    editorOptionsBottomBar: MemeEditorOptions,
    editorSelectionOptionsBottomBar: MemeEditorSelectionOptions,
    isBackDialogVisible: Boolean,
    shouldShowEditOptions: Boolean,
    onBackConfirmClickTopBar: (MemeEvent.TopBarEvent) -> Unit,
    onCancelClickDialog: (MemeEvent.TopBarEvent) -> Unit,
    onBackClickDialog: (MemeEvent.TopBarEvent) -> Unit,
    onAddTextBottomBar: (MemeEvent.EditorEvent) -> Unit,
    onSaveImageBottomBar: (MemeEvent.EditorEvent) -> Unit,
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

    val scope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    var imageContentSize by remember { mutableStateOf(Size.Zero) }
    var imageContentOffset by remember { mutableStateOf(Offset.Zero) }

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
                graphicsLayer = graphicsLayer,
                imageContentSize = imageContentSize,
                imageContentOffset = imageContentOffset,
                onPositionUpdate = onPositionUpdateEditor,
                onTextBoxClick = onTextBoxClickEditor,
                onTextBoxCloseClick = onTextBoxCloseClickEditor,
                onTextBoxTextChange = onTextBoxTextChangeEditor,
                onDeselectClick = onDeselectClickEditor,
                onImageSizeUpdate = { size, offset ->
                    imageContentSize = size
                    imageContentOffset = offset
                },
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
                    onSaveImage = {
                        scope.launch {
                            onSaveImageBottomBar(
                                MemeEvent.EditorEvent.SaveImage(
                                    imageBitmap = graphicsLayer.toImageBitmap(),
                                    offset = imageContentOffset,
                                    size = imageContentSize,
                                )
                            )
                        }
                    },
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
