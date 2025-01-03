package fyi.manpreet.composablememes.ui.meme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.rememberGraphicsLayer
import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.home.components.bottomsheet.MemeShareBottomSheet
import fyi.manpreet.composablememes.ui.meme.components.bottombar.MemeBottomBar
import fyi.manpreet.composablememes.ui.meme.components.bottombar.MemeBottomBarEditOptions
import fyi.manpreet.composablememes.ui.meme.components.dialog.BackConfirmationDialog
import fyi.manpreet.composablememes.ui.meme.components.meme.MemeImage
import fyi.manpreet.composablememes.ui.meme.components.topbar.MemeTopBar
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorSelectionOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.MemeTextBox
import fyi.manpreet.composablememes.ui.meme.state.ShareOption
import kotlinx.coroutines.launch

@Composable
fun MemeScreenContent(
    meme: Meme?,
    textBoxes: List<MemeTextBox>,
    editorOptions: MemeEditorOptions,
    editorSelectionOptionsBottomBar: MemeEditorSelectionOptions,
    isBackDialogVisible: Boolean,
    shouldShowEditOptions: Boolean,
    shareOptions: List<ShareOption>,
    onBackConfirmClickTopBar: (MemeEvent.TopBarEvent) -> Unit,
    onCancelClickDialog: (MemeEvent.TopBarEvent) -> Unit,
    onBackClickDialog: (MemeEvent.TopBarEvent) -> Unit,
    onAddTextBottomBar: (MemeEvent.EditorEvent) -> Unit,
    onPositionUpdateEditor: (MemeEvent.EditorEvent) -> Unit,
    onSizeUpdateEditor: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxClickEditor: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxCloseClickEditor: (MemeEvent.EditorEvent) -> Unit,
    onTextBoxTextChangeEditor: (MemeEvent.EditorEvent) -> Unit,
    onDeselectClickEditor: (MemeEvent.EditorEvent) -> Unit,
    onUndoEditor: (MemeEvent.EditorEvent) -> Unit,
    onRedoEditor: (MemeEvent.EditorEvent) -> Unit,
    onEditorOptionsItemClickBottomBar: (MemeEvent.EditorOptionsBottomBarEvent) -> Unit,
    onFontItemSelectBottomBar: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit,
    onFontSizeChangeBottomBar: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit,
    onFontColorSelectBottomBar: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit,
    onSaveImageBottomSheet: (MemeEvent.SaveEvent) -> Unit,
) {

    if (meme == null) return

    val scope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    val shareSheetState: ModalBottomSheetState = rememberModalBottomSheetState(initialDetent = Hidden)

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
                imageContentSize = editorOptions.imageContentSize,
                imageContentOffset = editorOptions.imageContentOffset,
                onPositionUpdate = onPositionUpdateEditor,
                onTextBoxClick = onTextBoxClickEditor,
                onTextBoxCloseClick = onTextBoxCloseClickEditor,
                onTextBoxTextChange = onTextBoxTextChangeEditor,
                onDeselectClick = onDeselectClickEditor,
                onEditorSizeUpdate = onSizeUpdateEditor,
            )

            if (shouldShowEditOptions) {
                MemeBottomBarEditOptions(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    editorOptions = editorOptions,
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
                    canUndo = editorOptions.isUndoEnabled,
                    canRedo = editorOptions.isRedoEnabled,
                    onSaveImage = { shareSheetState.currentDetent = SheetDetent.FullyExpanded },
                    onUndo = onUndoEditor,
                    onRedo = onRedoEditor,
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

    MemeShareBottomSheet(
        state = shareSheetState,
        shareOptions = shareOptions,
        onShareClick = { type ->
            scope.launch {
                onSaveImageBottomSheet(
                    MemeEvent.SaveEvent.SaveImage(
                        imageBitmap = graphicsLayer.toImageBitmap(),
                        offset = editorOptions.imageContentOffset,
                        size = editorOptions.imageContentSize,
                        type = type,
                    )
                )
            }
            shareSheetState.currentDetent = Hidden
        },
    )
}
