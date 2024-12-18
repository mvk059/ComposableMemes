package fyi.manpreet.composablememes.ui.home.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Scrim
import com.composables.core.Sheet
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.home.components.item.MemeItem
import fyi.manpreet.composablememes.ui.home.components.loader.Loader
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.StringResource

@Composable
fun MemeListBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState,
    memeList: List<Meme>,
    isLoading: Boolean,
    onMemeSelected: (HomeEvent.BottomSheetEvent.OnMemeSelect) -> Unit,
    searchMode: Boolean,
    toggleSearchMode: (HomeEvent.BottomSheetEvent) -> Unit,
    searchText: String,
    onSearchTextChange: (HomeEvent.BottomSheetEvent) -> Unit,
    inputPlaceHolder: StringResource,
    memesListSize: Int,
    onDismiss: () -> Unit,
) {

    ModalBottomSheet(
        state = sheetState,
        onDismiss = onDismiss,
    ) {
        Scrim()
        Sheet(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)
                            .asPaddingValues()
                    )
                    .navigationBarsPadding()
                    .imePadding(),
                contentAlignment = Alignment.TopCenter
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DragIndication(
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.large)
                            .background(color = MaterialTheme.colorScheme.outline)
                            .width(MaterialTheme.spacing.largeXL)
                            .height(MaterialTheme.spacing.extraSmall)
                    )

                    if (isLoading) {
                        Loader()
                        return@Box
                    }

                    MemeListBottomSheetTopBar(
                        searchMode = searchMode,
                        toggleSearchMode = toggleSearchMode,
                        searchText = searchText,
                        onSearchTextChange = onSearchTextChange,
                        inputPlaceHolder = inputPlaceHolder,
                        memesListSize = memesListSize,
                    )

                    ListContent(
                        memeList = memeList,
                        onMemeSelected = onMemeSelected,
                    )
                }
            }
        }
    }
}

@Composable
private fun ListContent(
    modifier: Modifier = Modifier,
    memeList: List<Meme>,
    onMemeSelected: (HomeEvent.BottomSheetEvent.OnMemeSelect) -> Unit,
) {

    LazyVerticalGrid(
        modifier = modifier.padding(top = MaterialTheme.spacing.large2XL),
        columns = GridCells.Fixed(2),
    ) {

        items(
            items = memeList,
            key = { memes -> memes.imageName.value },
        ) { meme ->

            MemeItem(
                modifier = Modifier.clickable {
                    onMemeSelected(HomeEvent.BottomSheetEvent.OnMemeSelect(meme))
                },
                meme = meme,
            )
        }

    }
}