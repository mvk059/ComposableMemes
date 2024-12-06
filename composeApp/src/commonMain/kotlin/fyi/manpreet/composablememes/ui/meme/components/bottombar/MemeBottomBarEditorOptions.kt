package fyi.manpreet.composablememes.ui.meme.components.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorSelectionOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.painterResource

@Composable
fun MemeBottomBarEditOptions(
    modifier: Modifier = Modifier,
    editorOptions: MemeEditorOptions,
    editorSelectionOptions: MemeEditorSelectionOptions,
    onFontClick: (MemeEvent.EditorOptionsBottomBarEvent) -> Unit,
    onFontItemSelect: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit,
    onFontSizeClick: (MemeEvent.EditorOptionsBottomBarEvent) -> Unit,
    onFontColorClick: (MemeEvent.EditorOptionsBottomBarEvent) -> Unit,
    onDoneClick: (MemeEvent.EditorOptionsBottomBarEvent) -> Unit,
    onCloseClick: (MemeEvent.EditorOptionsBottomBarEvent) -> Unit,
    onFontSizeChange: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit
) {

    Column(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {

        when (editorOptions.selectedOption) {
            MemeEvent.EditorOptionsBottomBarEvent.Close -> {}
            MemeEvent.EditorOptionsBottomBarEvent.Done -> {}
            MemeEvent.EditorOptionsBottomBarEvent.Font ->
                MemeBottomBarEditorFont(
                    modifier = Modifier,
                    fonts = editorSelectionOptions.fonts,
                    onFontClick = onFontItemSelect,
                )

            MemeEvent.EditorOptionsBottomBarEvent.FontColor -> {}
            MemeEvent.EditorOptionsBottomBarEvent.FontSize ->
                MemeBottomBarEditorFontSize(
                    fontSize = editorSelectionOptions.fontSize,
                    onFontSizeChange = onFontSizeChange,
                )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            IconButton(
                onClick = { onCloseClick(MemeEvent.EditorOptionsBottomBarEvent.Close) }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {

                editorOptions.options.forEach { option ->
                    val background =
                        if (option.type == editorOptions.selectedOption) MaterialTheme.colorScheme.surfaceContainerHigh
                        else Color.Transparent

                    IconButton(
                        modifier = Modifier.background(
                            color = background,
                            shape = RoundedCornerShape(MaterialTheme.spacing.smallMedium)
                        ),
                        onClick = { onFontClick(option.type) }
                    ) {
                        Icon(
                            painterResource(resource = option.icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiary,
                        )
                    }
                }
            }

            IconButton(
                onClick = { onDoneClick(MemeEvent.EditorOptionsBottomBarEvent.Done) }
            ) {
                Icon(
                    Icons.Default.Done,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary,
                )
            }
        }
    }
}
