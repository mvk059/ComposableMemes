package fyi.manpreet.composablememes.ui.meme.components.bottombar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import composablememes.composeapp.generated.resources.*
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.gradient
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MemeBottomBar(
    modifier: Modifier = Modifier,
    onAddText: (MemeEvent.EditorEvent) -> Unit,
    onSaveImage: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val gradient =
        if (isPressed) MaterialTheme.gradient.pressed
        else MaterialTheme.gradient.default

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .height(MaterialTheme.spacing.bottomBarSize),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        IconButton(onClick = {}) {
            Icon(
                painter = painterResource(Res.drawable.ic_undo),
                contentDescription = stringResource(Res.string.meme_bottom_bar_undo_cd)
            )
        }

        IconButton(onClick = {}) {
            Icon(
                painterResource(resource = Res.drawable.ic_redo),
                contentDescription = stringResource(Res.string.meme_bottom_bar_redo_cd)
            )
        }

        Button(
            modifier = Modifier.border(
                BorderStroke(1.dp, MaterialTheme.gradient.borderButton),
                MaterialTheme.shapes.medium
            ),
            colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Transparent),
            onClick = { onAddText(MemeEvent.EditorEvent.AddTextBox) },
            content = {
                Text(
                    text = stringResource(Res.string.meme_bottom_bar_add_text),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        )

        Button(
            modifier = Modifier.background(
                brush = gradient,
                shape = MaterialTheme.shapes.medium
            ),
            colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Transparent),
            interactionSource = interactionSource,
            onClick = { onSaveImage() },
            content = {
                Text(
                    text = stringResource(Res.string.meme_bottom_bar_save_meme),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.fixedAccentColors.onPrimaryFixed,
                )
            }
        )
    }
}
