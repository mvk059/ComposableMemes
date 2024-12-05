package fyi.manpreet.composablememes.ui.home.components.empty

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.home_add_meme
import composablememes.composeapp.generated.resources.ic_meme_man
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreenEmpty(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(Res.drawable.ic_meme_man),
            contentDescription = null,
        )

        Text(
            modifier = Modifier.padding(top = MaterialTheme.spacing.large),
            text = stringResource(Res.string.home_add_meme),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.outline
            )
        )
    }
}
