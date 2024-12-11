package fyi.manpreet.composablememes

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fyi.manpreet.composablememes.navigation.HomeDestination
import fyi.manpreet.composablememes.navigation.MemeDestination
import fyi.manpreet.composablememes.ui.home.HomeViewModel
import fyi.manpreet.composablememes.ui.home.HomeScreen
import fyi.manpreet.composablememes.ui.meme.MemeScreen
import fyi.manpreet.composablememes.ui.theme.MemeTheme
import fyi.manpreet.composablememes.util.MemeConstants
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    viewModel: HomeViewModel = koinViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val homeState = viewModel.homeState.collectAsStateWithLifecycle()
    val allMemes = viewModel.allMemesList.collectAsStateWithLifecycle()

    MemeTheme {

        NavHost(
            navController = navController,
            startDestination = HomeDestination,
        ) {

            composable<HomeDestination> {

                HomeScreen(
                    navController = navController,
                    homeState = homeState.value,
                    memeListBottomSheet = allMemes.value,
                    onFabClick = viewModel::onEvent,
                    toggleSearchModeBottomSheet = viewModel::onEvent,
                    onSearchTextChangeBottomSheet = viewModel::onEvent,
                    onMemeSelectBottomSheet = {
                        viewModel.onEvent(it)
                        navController.navigate(MemeDestination(it.meme.imageName))
                    },
                    onFavoriteClick = viewModel::onEvent,
                    onSelectClick = viewModel::onEvent,
                    onEnterSelectionMode = viewModel::onEvent,
                    onSelectedSortType = viewModel::onEvent,
                    onCancelClickTopBar = viewModel::onEvent,
                    onShareClickTopBar = viewModel::onEvent,
                    onDeleteClickTopBar = viewModel::onEvent,
                    onCancelClickDialog = viewModel::onEvent,
                    onDeleteClickDialog = viewModel::onEvent,
                    onReload = viewModel::onEvent,
                )
            }

            composable<MemeDestination> {
                val args = it.toRoute<MemeDestination>()
                MemeScreen(
                    memeName = args.memeName,
                    navigateBack = {
                        navController.previousBackStackEntry?.savedStateHandle?.set(MemeConstants.NAVIGATE_BACK_RELOAD, true)
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}
