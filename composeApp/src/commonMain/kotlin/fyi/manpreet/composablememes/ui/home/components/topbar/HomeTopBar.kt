package fyi.manpreet.composablememes.ui.home.components.topbar

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.home_appbar_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
) {

    TopAppBar(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLow),
        title = {
            Text(stringResource(Res.string.home_appbar_title))
        },
        actions = {}
    )
}