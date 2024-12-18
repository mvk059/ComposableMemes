package fyi.manpreet.composablememes.ui.meme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fyi.manpreet.composablememes.data.model.MemeImageName
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MemeScreen(
    viewModel: MemeViewModel = koinViewModel(),
    memeName: MemeImageName,
    navigateBack: () -> Unit,
) {
    val memeState = viewModel.memeState.collectAsStateWithLifecycle()
    val onNavigateBack = viewModel.navigateBackStatus.collectAsStateWithLifecycle()

    // TODO Remove and test this (onStart)
    LaunchedEffect(Unit) {
        viewModel.loadMeme(memeName)
    }

    LaunchedEffect(onNavigateBack.value) {
        if (onNavigateBack.value) {
            navigateBack()
        }
    }

    MemeScreenContent(
        meme = memeState.value.meme,
        textBoxes = memeState.value.textBoxes,
        editorOptions = memeState.value.editorOptions,
        editorSelectionOptionsBottomBar = memeState.value.editorSelectionOptions,
        isBackDialogVisible = memeState.value.isBackDialogVisible,
        shouldShowEditOptions = memeState.value.shouldShowEditOptions,
        shareOptions = memeState.value.shareOptions,
        onBackConfirmClickTopBar = viewModel::onEvent,
        onCancelClickDialog = viewModel::onEvent,
        onBackClickDialog = viewModel::onEvent,
        onAddTextBottomBar = viewModel::onEvent,
        onPositionUpdateEditor = viewModel::onEvent,
        onSizeUpdateEditor = viewModel::onEvent,
        onTextBoxClickEditor = viewModel::onEvent,
        onTextBoxCloseClickEditor = viewModel::onEvent,
        onTextBoxTextChangeEditor = viewModel::onEvent,
        onDeselectClickEditor = viewModel::onEvent,
        onUndoEditor = viewModel::onEvent,
        onRedoEditor = viewModel::onEvent,
        onEditorOptionsItemClickBottomBar = viewModel::onEvent,
        onFontItemSelectBottomBar = viewModel::onEvent,
        onFontSizeChangeBottomBar = viewModel::onEvent,
        onFontColorSelectBottomBar = viewModel::onEvent,
        onSaveImageBottomSheet = viewModel::onEvent,
    )
}