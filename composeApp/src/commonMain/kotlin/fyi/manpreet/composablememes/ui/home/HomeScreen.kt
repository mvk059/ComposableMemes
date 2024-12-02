package fyi.manpreet.composablememes.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import fyi.manpreet.composablememes.data.mapper.Peek
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.home.components.bottomsheet.MemeListBottomSheet
import fyi.manpreet.composablememes.ui.home.components.empty.HomeScreenEmpty
import fyi.manpreet.composablememes.ui.home.components.fab.HomeFloatingActionButton
import fyi.manpreet.composablememes.ui.home.components.item.MemeItem
import fyi.manpreet.composablememes.ui.home.components.topbar.HomeTopBar
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import fyi.manpreet.composablememes.ui.home.state.MemeListBottomSheet
import fyi.manpreet.composablememes.ui.theme.spacing

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    memes: List<Meme>,
    memeListBottomSheet: MemeListBottomSheet,
    onFabClick: (HomeEvent.BottomSheetEvent) -> Unit,
    toggleSearchModeBottomSheet: (HomeEvent.BottomSheetEvent) -> Unit,
    onSearchTextChangeBottomSheet: (HomeEvent.BottomSheetEvent) -> Unit,
    onMemeSelected: (HomeEvent.BottomSheetEvent) -> Unit,
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
            HomeTopBar()
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

        if (memes.isEmpty()) {
            HomeScreenEmpty()
            return@Scaffold
        }

        LazyVerticalGrid(
            modifier = modifier.padding(top = MaterialTheme.spacing.large3XL),
            columns = GridCells.Fixed(2),
        ) {

            items(
                items = memes,
                key = { memes -> memes.id },
            ) { meme ->

                MemeItem(
                    modifier = Modifier.clickable {},
                    meme = meme,
                )
            }

        }

    }

    MemeListBottomSheet(
        sheetState = sheetState,
        memeList = memeListBottomSheet.memes,
        onMemeSelected = {
            onMemeSelected(it)
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

}