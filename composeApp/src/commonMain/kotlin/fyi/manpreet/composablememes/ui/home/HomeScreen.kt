package fyi.manpreet.composablememes.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.home_appbar_title
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.home.components.fab.HomeFloatingActionButton
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    memes: List<Meme>,
) {

    Scaffold(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLow),
                title = {
                    Text(stringResource(Res.string.home_appbar_title))
                },
                actions = {}
            )
        },
        floatingActionButton = {
            HomeFloatingActionButton()
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

}