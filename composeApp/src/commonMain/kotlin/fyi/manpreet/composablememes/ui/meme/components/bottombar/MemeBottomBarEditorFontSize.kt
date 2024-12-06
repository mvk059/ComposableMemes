package fyi.manpreet.composablememes.ui.meme.components.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemeBottomBarEditorFontSize(
    modifier: Modifier = Modifier,
    fontSize: Float,
    onFontSizeChange: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit
) {

    Row(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLow),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
            text = "aA",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiary,
        )

        Slider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.spacing.small),
            value = fontSize,
            onValueChange = {
                onFontSizeChange(MemeEvent.EditorSelectionOptionsBottomBarEvent.FontSize(it))
            },
            thumb = {
                Box(
                    modifier = Modifier
                        .padding(0.dp)
                        .background(MaterialTheme.fixedAccentColors.secondaryFixedDim, CircleShape)
                        .size(MaterialTheme.spacing.medium)
                )
            },
            track = { sliderState ->
                // Calculate fraction of the slider that is active
                val fraction by remember {
                    derivedStateOf {
                        (sliderState.value - sliderState.valueRange.start) / (sliderState.valueRange.endInclusive - sliderState.valueRange.start)
                    }
                }

                Box(Modifier.fillMaxWidth()) {
                    Box(
                        Modifier
                            .fillMaxWidth(fraction)
                            .align(Alignment.CenterStart)
                            .height(1.dp)
                            .padding(end = 0.dp)
                            .background(
                                MaterialTheme.fixedAccentColors.secondaryFixedDim,
                                CircleShape
                            )
                    )
                    Box(
                        Modifier
                            .fillMaxWidth(1f - fraction)
                            .align(Alignment.CenterEnd)
                            .height(1.dp)
                            .padding(start = 0.dp)
                            .background(
                                MaterialTheme.fixedAccentColors.secondaryFixedDim,
                                CircleShape
                            )
                    )
                }
            }
        )

        Text(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
            text = "aA",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onTertiary,
        )
    }

}