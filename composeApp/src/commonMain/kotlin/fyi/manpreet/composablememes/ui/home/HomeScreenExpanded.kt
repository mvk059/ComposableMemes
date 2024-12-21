package fyi.manpreet.composablememes.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.home_expanded_appbar_title
import fyi.manpreet.composablememes.ui.home.components.item.MemeItem
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import fyi.manpreet.composablememes.ui.home.state.MemeListBottomSheet
import fyi.manpreet.composablememes.ui.theme.spacing
import fyi.manpreet.composablememes.util.MemeConstants
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenExpanded(
    modifier: Modifier = Modifier,
    navController: NavController,
    memeListBottomSheet: MemeListBottomSheet,
    onMemeSelectBottomSheet: (HomeEvent.BottomSheetEvent.OnMemeSelect) -> Unit,
    onReload: (HomeEvent) -> Unit,
) {

    val shouldReload = navController.currentBackStackEntry?.savedStateHandle
        ?.getStateFlow(MemeConstants.NAVIGATE_BACK_RELOAD, false)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(shouldReload?.value) {
        if (shouldReload?.value == true) {
            onReload(HomeEvent.OnReload)
            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = MemeConstants.NAVIGATE_BACK_RELOAD,
                value = false
            )
        }
    }

    Scaffold(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
        topBar = {
            TopAppBar(
                modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLow),
                title = { Text(stringResource(Res.string.home_expanded_appbar_title)) },
            )
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.small),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                LazyVerticalGrid(
                    modifier = modifier.padding(top = MaterialTheme.spacing.large2XL),
                    columns = GridCells.Adaptive(minSize = 200.dp),
                ) {

                    items(
                        items = memeListBottomSheet.memes,
                        key = { memes -> memes.imageName.value },
                    ) { meme ->

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            MemeItem(
                                modifier = Modifier
                                    .clickable {
                                        onMemeSelectBottomSheet(
                                            HomeEvent.BottomSheetEvent.OnMemeSelect(
                                                meme
                                            )
                                        )
                                    },
                                meme = meme,
                            )

                            Text(
                                text = meme.imageName.value,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = MaterialTheme.spacing.small,
                                        bottom = MaterialTheme.spacing.large
                                    ),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                minLines = 2,
                                maxLines = 2,
                            )
                        }
                    }
                }
            }
        }
    }
}
