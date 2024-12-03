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
    memeId: Long,
    navigateBack: () -> Unit,
) {
    val memeState = viewModel.memeState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMeme(memeId)
    }

    MemeScreenContent(
        modifier = modifier,
        isBackDialogVisible = memeState.value.isBackDialogVisible,
        onBackConfirmClickTopBar = viewModel::onEvent,
        onCancelClickDialog = viewModel::onEvent,
        onBackClickDialog = navigateBack,
    )

}