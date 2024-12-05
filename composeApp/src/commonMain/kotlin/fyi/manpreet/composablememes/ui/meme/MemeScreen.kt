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
        isBackDialogVisible = memeState.value.isBackDialogVisible,
        onBackConfirmClickTopBar = viewModel::onEvent,
        onCancelClickDialog = viewModel::onEvent,
        onBackClickDialog = navigateBack,
        onAddTextBottomBar = viewModel::onEvent,
        onPositionUpdateEditor = viewModel::onEvent,
        onTextBoxClickEditor = viewModel::onEvent,
        onTextBoxCloseClickEditor = viewModel::onEvent,
        onDeselectClickEditor = viewModel::onEvent,
    )

}