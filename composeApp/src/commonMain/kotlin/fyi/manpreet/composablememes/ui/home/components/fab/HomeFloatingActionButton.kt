package fyi.manpreet.composablememes.ui.home.components.fab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.meme_fab_cd
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.gradient
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val gradient =
        if (isPressed) MaterialTheme.gradient.pressed
        else MaterialTheme.gradient.default

    Box(
        modifier = modifier
            .defaultMinSize(
                minWidth = MaterialTheme.spacing.fabContainerWidth,
                minHeight = MaterialTheme.spacing.fabContainerHeight,
            )
            .background(brush = gradient, shape = FloatingActionButtonDefaults.shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {

        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(Res.string.meme_fab_cd),
            tint = MaterialTheme.fixedAccentColors.onPrimaryFixed,
        )
    }

}
