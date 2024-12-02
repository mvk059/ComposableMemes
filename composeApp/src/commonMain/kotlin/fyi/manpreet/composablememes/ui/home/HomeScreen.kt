package fyi.manpreet.composablememes.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import fyi.manpreet.composablememes.data.mapper.Peek
import fyi.manpreet.composablememes.ui.home.components.bottomsheet.MemeListBottomSheet
import fyi.manpreet.composablememes.ui.home.components.delete.DeleteDialog
import fyi.manpreet.composablememes.ui.home.components.empty.HomeScreenEmpty
import fyi.manpreet.composablememes.ui.home.components.fab.HomeFloatingActionButton
import fyi.manpreet.composablememes.ui.home.components.item.MemeItem
import fyi.manpreet.composablememes.ui.home.components.topbar.HomeTopBar
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import fyi.manpreet.composablememes.ui.home.state.HomeState
import fyi.manpreet.composablememes.ui.home.state.MemeListBottomSheet
import fyi.manpreet.composablememes.ui.theme.spacing

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
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
    onDeleteClickDialog: (HomeEvent.TopBarEvent) -> Unit
) {

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
    ) {

        if (homeState.memes.isEmpty()) {
            HomeScreenEmpty()
            return@Scaffold
        }

        LazyVerticalGrid(
            modifier = modifier.padding(top = MaterialTheme.spacing.large3XL),
            columns = GridCells.Fixed(2),
        ) {

            items(
                items = homeState.memes,
                key = { memes -> memes.id },
            ) { meme ->

                MemeItem(
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                onEnterSelectionMode(
                                    HomeEvent.MemeListEvent.OnEnterSelectionMode(
                                        meme.id
                                    )
                                )
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
    }

    MemeListBottomSheet(
        sheetState = sheetState,
        memeList = memeListBottomSheet.memes,
        onMemeSelected = {
            onMemeSelectBottomSheet(it)
            dismissBottomSheet()
        },
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