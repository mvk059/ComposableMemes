package fyi.manpreet.composablememes.ui.meme

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MemeScreen(
    modifier: Modifier = Modifier,
    viewModel: MemeViewModel = koinViewModel(),
    memeId: Long,
) {
    val meme = viewModel.meme.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadMeme(memeId)
    }

    Scaffold(
        topBar = {

        },
        bottomBar = {

        }
    ) {

        Text("Hello")
    }

}