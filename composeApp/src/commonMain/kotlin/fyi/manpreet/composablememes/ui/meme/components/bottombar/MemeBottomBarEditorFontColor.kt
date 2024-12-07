package fyi.manpreet.composablememes.ui.meme.components.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorSelectionOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.theme.spacing

@Composable
fun MemeBottomBarEditorFontColor(
    modifier: Modifier = Modifier,
    colors: List<MemeEditorSelectionOptions.FontColor>,
    onFontColorSelect: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit
) {

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialTheme.spacing.large3XL)
            .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {

        items(colors) { color ->

            Row(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .padding(top = MaterialTheme.spacing.medium)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box {

                    if (color.isSelected) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.Center)
                                .padding(bottom = 0.dp)
                                .blur(30.dp, edgeTreatment = BlurredEdgeTreatment(CircleShape))
                                .background(color = color.color, shape = CircleShape)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(MaterialTheme.spacing.large)
                            .align(Alignment.Center)
                            .background(color = color.color, shape = CircleShape)
                            .clickable {
                                onFontColorSelect(
                                    MemeEvent.EditorSelectionOptionsBottomBarEvent.FontColor(color.id)
                                )
                            }
                    )
                }
            }
        }

    }
}