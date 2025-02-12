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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.ic_redo
import composablememes.composeapp.generated.resources.ic_undo
import composablememes.composeapp.generated.resources.meme_bottom_bar_add_text
import composablememes.composeapp.generated.resources.meme_bottom_bar_redo_cd
import composablememes.composeapp.generated.resources.meme_bottom_bar_save_meme
import composablememes.composeapp.generated.resources.meme_bottom_bar_undo_cd
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.gradient
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemeBottomBar(
    modifier: Modifier = Modifier,
    canUndo: Boolean,
    canRedo: Boolean,
    onAddText: (MemeEvent.EditorEvent) -> Unit,
    onSaveImage: () -> Unit,
    onUndo: (MemeEvent.EditorEvent) -> Unit,
    onRedo: (MemeEvent.EditorEvent) -> Unit,
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

        IconButton(
            enabled = canUndo,
            onClick = { onUndo(MemeEvent.EditorEvent.Undo) }
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_undo),
                contentDescription = stringResource(Res.string.meme_bottom_bar_undo_cd),
                tint =
                if (canUndo) MaterialTheme.fixedAccentColors.secondaryFixedDim
                else MaterialTheme.colorScheme.background
            )
        }

        IconButton(
            enabled = canRedo,
            onClick = { onRedo(MemeEvent.EditorEvent.Redo) }
        ) {
            Icon(
                painterResource(resource = Res.drawable.ic_redo),
                contentDescription = stringResource(Res.string.meme_bottom_bar_redo_cd),
                tint =
                if (canRedo) MaterialTheme.fixedAccentColors.secondaryFixedDim
                else MaterialTheme.colorScheme.background
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

        CompositionLocalProvider(LocalRippleConfiguration provides null) {

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
}
