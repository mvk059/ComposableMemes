package fyi.manpreet.composablememes.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import fyi.manpreet.composablememes.data.mapper.Peek
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.home.components.bottomsheet.MemeListBottomSheet
import fyi.manpreet.composablememes.ui.home.components.fab.HomeFloatingActionButton
import fyi.manpreet.composablememes.ui.home.components.topbar.HomeTopBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    memes: List<Meme>,
) {

    val sheetState = rememberModalBottomSheetState(
        initialDetent = Hidden,
        detents = listOf(Hidden, Peek, FullyExpanded)
    )

    Scaffold(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
        topBar = {
            HomeTopBar()
        },
        floatingActionButton = {
            HomeFloatingActionButton(
                onClick = { sheetState.currentDetent = Peek }
            )
        }
    ) {

        if (memes.isEmpty()) {
            HomeScreenEmpty()
            return@Scaffold
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {

            items(
                items = memes,
                key = { memes -> memes.imageUrl },
            ) { meme ->

//                Image(
//                    modifier = Modifier.size(120.dp),
//                    painter = painterResource(Res.allDrawableResources[meme.imageUrl]!!),
//                    contentDescription = null,
//                    contentScale = ContentScale.FillBounds
//                )
            }

        }

    }

    MemeListBottomSheet(
        sheetState = sheetState,
        memes = memes,
    )

}