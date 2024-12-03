package fyi.manpreet.composablememes

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fyi.manpreet.composablememes.ui.home.HomeViewModel
import fyi.manpreet.composablememes.ui.home.HomeScreen
import fyi.manpreet.composablememes.ui.theme.MemeTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val homeState = viewModel.homeState.collectAsStateWithLifecycle()
    val allMemes = viewModel.allMemesList.collectAsStateWithLifecycle()

    MemeTheme {

        HomeScreen(
            homeState = homeState.value,
            memeListBottomSheet = allMemes.value,
            onFabClick = viewModel::onEvent,
            toggleSearchModeBottomSheet = viewModel::onEvent,
            onSearchTextChangeBottomSheet = viewModel::onEvent,
            onMemeSelected = viewModel::onEvent,
            onFavoriteClick = viewModel::onEvent,
            onSelectClick = viewModel::onEvent,
            onEnterSelectionMode = viewModel::onEvent,
            onSelectedSortType = viewModel::onEvent,
            onCancelClick = viewModel::onEvent,
            onShareClick = viewModel::onEvent,
            onDeleteClick = viewModel::onEvent,
        )
    }
}
