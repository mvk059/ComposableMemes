package fyi.manpreet.composablememes

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fyi.manpreet.composablememes.navigation.HomeDestination
import fyi.manpreet.composablememes.navigation.HomeWasmDestination
import fyi.manpreet.composablememes.navigation.MemeDestination
import fyi.manpreet.composablememes.ui.home.HomeScreen
import fyi.manpreet.composablememes.ui.home.HomeScreenExpanded
import fyi.manpreet.composablememes.ui.home.HomeViewModel
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
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
    platform: Platform,
) {
    val homeState = viewModel.homeState.collectAsStateWithLifecycle()
    val allMemes = viewModel.allMemesList.collectAsStateWithLifecycle()

    MemeTheme {

        val startDestination: Any = when (platform) {
            Platform.Android, Platform.Ios -> HomeDestination
            Platform.WasmJs -> HomeWasmDestination
        }

        NavHost(
            navController = navController,
            startDestination = startDestination,
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
                        navController.navigate(
                            MemeDestination(
                                memeName = it.meme.imageName.value,
                                memePath = it.meme.path?.value ?: "",
                                width = it.meme.width,
                                height = it.meme.height
                            )
                        )
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

            composable<HomeWasmDestination> {
                viewModel.onEvent(HomeEvent.BottomSheetEvent.OnFabClick)

                HomeScreenExpanded(
                    navController = navController,
                    memeListBottomSheet = allMemes.value,
                    onMemeSelectBottomSheet = {
                        viewModel.onEvent(it)
                        navController.navigate(
                            MemeDestination(
                                memeName = it.meme.imageName.value,
                                memePath = it.meme.path?.value ?: "",
                                width = it.meme.width,
                                height = it.meme.height
                            )
                        )
                    },
                    onReload = viewModel::onEvent,
                )
            }

            composable<MemeDestination> {
                val args = it.toRoute<MemeDestination>()
                MemeScreen(
                    memeName = args,
                    navigateBack = {
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            key = MemeConstants.NAVIGATE_BACK_RELOAD,
                            value = true
                        )
                        navController.popBackStack()
                    },
                )
            }
        }
    }
}
