package fyi.manpreet.composablememes.ui.meme.components.topbar

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.meme_appbar_title
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemeTopBar(
    modifier: Modifier = Modifier,
    onBackClick: (MemeEvent.TopBarEvent) -> Unit,
) {

    CenterAlignedTopAppBar(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLow),
        title = {
            Text(
                text = stringResource(Res.string.meme_appbar_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { onBackClick(MemeEvent.TopBarEvent.BackConfirm) },
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            )
        }
    )
}
