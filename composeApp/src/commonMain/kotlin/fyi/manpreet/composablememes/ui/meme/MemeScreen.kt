package fyi.manpreet.composablememes.ui.meme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MemeScreen(
    modifier: Modifier = Modifier,
    viewModel: MemeViewModel = koinViewModel(),
    memeName: String,
    navigateBack: () -> Unit,
) {
    val memeState = viewModel.memeState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMeme(memeName)
    }

    MemeScreenContent(
        modifier = modifier,
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
        onPositionUpdateEditor = viewModel::onEvent,
        onTextBoxClickEditor = viewModel::onEvent,
        onTextBoxCloseClickEditor = viewModel::onEvent,
        onDeselectClickEditor = viewModel::onEvent,
        onFontClickBottomBar = viewModel::onEvent,
        onFontItemSelectBottomBar = viewModel::onEvent,
        onFontSizeClickBottomBar = viewModel::onEvent,
        onFontColorClickBottomBar = viewModel::onEvent,
        onDoneClickBottomBar = viewModel::onEvent,
        onCloseClickBottomBar = viewModel::onEvent,
    )

}