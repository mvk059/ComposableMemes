package fyi.manpreet.composablememes.ui.meme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MemeScreen(
    viewModel: MemeViewModel = koinViewModel(),
    memeName: String,
    navigateBack: () -> Unit,
) {
    val memeState = viewModel.memeState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMeme(memeName)
    }

    MemeScreenContent(
        meme = memeState.value.meme,
        textBoxes = memeState.value.textBoxes,
        editorOptionsBottomBar = memeState.value.editorOptions,
        editorSelectionOptionsBottomBar = memeState.value.editorSelectionOptions,
        isBackDialogVisible = memeState.value.isBackDialogVisible,
        shouldShowEditOptions = memeState.value.shouldShowEditOptions,
        onBackConfirmClickTopBar = viewModel::onEvent,
        onCancelClickDialog = viewModel::onEvent,
        onBackClickDialog = navigateBack,
        onAddTextBottomBar = viewModel::onEvent,
        onSaveImageBottomBar = viewModel::onEvent,
        onPositionUpdateEditor = viewModel::onEvent,
        onTextBoxClickEditor = viewModel::onEvent,
        onTextBoxCloseClickEditor = viewModel::onEvent,
        onTextBoxTextChangeEditor = viewModel::onEvent,
        onDeselectClickEditor = viewModel::onEvent,
        onEditorOptionsItemClickBottomBar = viewModel::onEvent,
        onFontItemSelectBottomBar = viewModel::onEvent,
        onFontSizeChangeBottomBar = viewModel::onEvent,
        onFontColorSelectBottomBar = viewModel::onEvent,
    )

}