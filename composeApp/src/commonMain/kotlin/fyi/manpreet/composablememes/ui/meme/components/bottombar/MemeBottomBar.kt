package fyi.manpreet.composablememes.ui.meme.components.bottombar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.gradient
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MemeBottomBar(
    modifier: Modifier = Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val gradient =
        if (isPressed) MaterialTheme.gradient.pressed
        else MaterialTheme.gradient.default

    BottomAppBar {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
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
                onClick = {},
                content = {
                    Text(
                        text = stringResource(Res.string.meme_bottom_bar_add_text),
                        style = MaterialTheme.typography.labelSmall,
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
                onClick = {},
                content = {
                    Text(
                        text = stringResource(Res.string.meme_bottom_bar_save_meme),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.fixedAccentColors.onPrimaryFixed,
                    )
                }
            )
        }
    }
    return
}