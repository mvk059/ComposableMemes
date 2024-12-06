package fyi.manpreet.composablememes.ui.meme.components.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorSelectionOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.theme.spacing

@Composable
fun MemeBottomBarEditorFont(
    modifier: Modifier = Modifier,
    fonts: List<MemeEditorSelectionOptions.Font>,
    onFontClick: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit
) {

    LazyRow(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
        items(fonts) { item ->
            val background =
                if (item.isSelected) MaterialTheme.colorScheme.surfaceContainerHigh
                else Color.Transparent

            Column(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.small, vertical = MaterialTheme.spacing.small),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier
                        .background(color = background, shape = MaterialTheme.shapes.medium)
                        .clickable { onFontClick(MemeEvent.EditorSelectionOptionsBottomBarEvent.Font(item.id)) },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = item.name,
                        modifier = Modifier.padding(MaterialTheme.spacing.small),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = item.type,
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.small),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiary,
                    )
                }
            }
        }
    }
}
