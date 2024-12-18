package fyi.manpreet.composablememes.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import fyi.manpreet.composablememes.ui.home.components.bottomsheet.MemeListBottomSheet
import fyi.manpreet.composablememes.ui.home.components.delete.DeleteDialog
import fyi.manpreet.composablememes.ui.home.components.empty.HomeScreenEmpty
import fyi.manpreet.composablememes.ui.home.components.fab.HomeFloatingActionButton
import fyi.manpreet.composablememes.ui.home.components.item.MemeItem
import fyi.manpreet.composablememes.ui.home.components.topbar.HomeTopBar
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import fyi.manpreet.composablememes.ui.home.state.HomeState
import fyi.manpreet.composablememes.ui.home.state.MemeListBottomSheet
import fyi.manpreet.composablememes.ui.theme.gradient
import fyi.manpreet.composablememes.ui.theme.spacing
import fyi.manpreet.composablememes.util.MemeConstants
import fyi.manpreet.composablememes.util.Peek

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeState: HomeState,
    memeListBottomSheet: MemeListBottomSheet,
    onFabClick: (HomeEvent.BottomSheetEvent) -> Unit,
    toggleSearchModeBottomSheet: (HomeEvent.BottomSheetEvent) -> Unit,
    onSearchTextChangeBottomSheet: (HomeEvent.BottomSheetEvent) -> Unit,
    onMemeSelectBottomSheet: (HomeEvent.BottomSheetEvent.OnMemeSelect) -> Unit,
    onFavoriteClick: (HomeEvent.MemeListEvent) -> Unit,
    onSelectClick: (HomeEvent.MemeListEvent) -> Unit = {},
    onEnterSelectionMode: (HomeEvent.MemeListEvent) -> Unit = {},
    onSelectedSortType: (HomeEvent.TopBarEvent) -> Unit,
    onCancelClickTopBar: (HomeEvent.TopBarEvent) -> Unit,
    onShareClickTopBar: (HomeEvent.TopBarEvent) -> Unit,
    onDeleteClickTopBar: (HomeEvent.TopBarEvent) -> Unit,
    onCancelClickDialog: (HomeEvent.TopBarEvent) -> Unit,
    onDeleteClickDialog: (HomeEvent.TopBarEvent) -> Unit,
    onReload: (HomeEvent) -> Unit,
) {

    val shouldReload = navController.currentBackStackEntry?.savedStateHandle
        ?.getStateFlow(MemeConstants.NAVIGATE_BACK_RELOAD, false)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(shouldReload?.value) {
        if (shouldReload?.value == true) {
            onReload(HomeEvent.OnReload)
            navController.currentBackStackEntry?.savedStateHandle?.set(MemeConstants.NAVIGATE_BACK_RELOAD, false)
        }
    }

    val sheetState = rememberModalBottomSheetState(
        initialDetent = Hidden,
        detents = listOf(Hidden, Peek, FullyExpanded)
    )

    fun dismissBottomSheet() {
        sheetState.currentDetent = Hidden
    }

    Scaffold(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
        topBar = {
            HomeTopBar(
                isSelectionMode = homeState.isSelectionMode,
                selectedItems = homeState.selectedItemsSize,
                sortTypes = homeState.sortTypes,
                selectedSortType = homeState.selectedSortType,
                onSelectedSortType = onSelectedSortType,
                onCancelClick = onCancelClickTopBar,
                onShareClick = onShareClickTopBar,
                onDeleteClick = onDeleteClickTopBar,
            )
        },
        floatingActionButton = {
            HomeFloatingActionButton(
                onClick = {
                    onFabClick(HomeEvent.BottomSheetEvent.OnFabClick)
                    sheetState.currentDetent = Peek
                }
            )
        }
    ) { innerPadding ->

        if (homeState.memes.isEmpty()) {
            HomeScreenEmpty(modifier = Modifier.padding(innerPadding))
            return@Scaffold
        }

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            LazyVerticalGrid(
                modifier = modifier,
                columns = GridCells.Fixed(2),
            ) {

                itemsIndexed(
                    items = homeState.memes,
                    key = { _, memes -> memes.id },
                ) { index, meme ->

                    val paddingBottom =
                        if (index == homeState.memes.lastIndex) MaterialTheme.spacing.large2XL
                        else MaterialTheme.spacing.none

                    MemeItem(
                        modifier = Modifier
                            .padding(bottom = paddingBottom)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        onEnterSelectionMode(HomeEvent.MemeListEvent.OnEnterSelectionMode(meme.id))
                                    },
                                    onTap = {
                                        onSelectClick(HomeEvent.MemeListEvent.OnMemeSelect(meme.id))
                                    }
                                )
                            },
                        meme = meme,
                        shouldShowFavorite = homeState.isSelectionMode.not(),
                        shouldShowSelection = homeState.isSelectionMode,
                        onFavoriteClick = onFavoriteClick,
                        onSelectClick = onSelectClick,
                    )
                }
            }

            // Shadow on top of the screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = MaterialTheme.spacing.medium)
                    .align(alignment = Alignment.TopCenter)
                    .background(brush = MaterialTheme.gradient.topScreenShadow)
            )

            // Shadow at the bottom of the screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = MaterialTheme.spacing.large4XL)
                    .align(alignment = Alignment.BottomCenter)
                    .background(brush = MaterialTheme.gradient.bottomScreenShadow)
            )
        }
    }

    MemeListBottomSheet(
        sheetState = sheetState,
        memeList = memeListBottomSheet.memes,
        onMemeSelected = {
            onMemeSelectBottomSheet(it)
            dismissBottomSheet()
        },
        isLoading = memeListBottomSheet.isLoading,
        searchMode = memeListBottomSheet.isSearchMode,
        toggleSearchMode = toggleSearchModeBottomSheet,
        searchText = memeListBottomSheet.searchText,
        onSearchTextChange = onSearchTextChangeBottomSheet,
        inputPlaceHolder = memeListBottomSheet.placeholder,
        memesListSize = memeListBottomSheet.memes.size,
        onDismiss = ::dismissBottomSheet
    )

    DeleteDialog(
        isVisible = homeState.isDeleteDialogVisible,
        selectedItems = homeState.selectedItemsSize,
        onDelete = onDeleteClickDialog,
        onCancel = onCancelClickDialog
    )

}