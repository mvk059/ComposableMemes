package fyi.manpreet.composablememes.ui.home.components.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.allDrawableResources
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MemeItem(
    modifier: Modifier = Modifier,
    meme: Meme,
) {

    Box(
        modifier = modifier
            .size(
                width = MaterialTheme.spacing.listItemWidth,
                height = MaterialTheme.spacing.listItemHeight,
            )
            .padding(MaterialTheme.spacing.small)
    ) {

        Image(
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(MaterialTheme.spacing.small)),
            painter = painterResource(Res.allDrawableResources[meme.imageName]!!),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }

}