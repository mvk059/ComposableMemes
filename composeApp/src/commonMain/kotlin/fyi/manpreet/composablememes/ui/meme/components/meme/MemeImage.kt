package fyi.manpreet.composablememes.ui.meme.components.meme

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.allDrawableResources
import fyi.manpreet.composablememes.data.model.Meme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun MemeImage(
    modifier: Modifier = Modifier,
    meme: Meme,
) {
    val image: DrawableResource = Res.allDrawableResources[meme.imageName] ?: return

    Image(
        modifier = modifier,
        painter = painterResource(image),
        contentDescription = "Meme Image",
        contentScale = ContentScale.Fit,
    )
}