package fyi.manpreet.composablememes.ui.home.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.home.components.loader.ShimmerBrush
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import fyi.manpreet.composablememes.ui.icon.CircleIcon
import fyi.manpreet.composablememes.ui.theme.gradient
import fyi.manpreet.composablememes.ui.theme.spacing

@Composable
fun MemeItem(
    modifier: Modifier = Modifier,
    meme: Meme,
    shouldShowFavorite: Boolean = false,
    shouldShowSelection: Boolean = false,
    onFavoriteClick: (HomeEvent.MemeListEvent) -> Unit = {},
    onSelectClick: (HomeEvent.MemeListEvent) -> Unit = {},
) {

    Box(
        modifier = modifier
            .size(
                width = MaterialTheme.spacing.listItemWidth,
                height = MaterialTheme.spacing.listItemHeight,
            )
            .padding(MaterialTheme.spacing.small)
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(meme.path?.value)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(MaterialTheme.spacing.small)),
            contentScale = ContentScale.Crop,
            placeholder = ShimmerBrush(),
        )

        if (shouldShowFavorite) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = MaterialTheme.gradient.favorite)
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(MaterialTheme.spacing.small)
                    .clickable { onFavoriteClick(HomeEvent.MemeListEvent.OnMemeFavorite(meme.id)) },
                imageVector = if (meme.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primaryContainer,
            )
        }

        if (shouldShowSelection) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = MaterialTheme.gradient.selection)
            )
            Icon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(MaterialTheme.spacing.small)
                    .clickable { onSelectClick(HomeEvent.MemeListEvent.OnMemeSelect(meme.id)) },
                imageVector = if (meme.isSelected) Icons.Default.CheckCircle else CircleIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primaryContainer,
            )
        }
    }

}