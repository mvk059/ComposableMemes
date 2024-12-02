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
    val allMemes = viewModel.allMemesList.collectAsStateWithLifecycle()

    MemeTheme {

        HomeScreen(
            memes = emptyList(),
            allMemes = allMemes.value,
            onEvent = viewModel::onEvent
        )
    }
}