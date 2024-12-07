package fyi.manpreet.composablememes.ui.meme.components.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fyi.manpreet.composablememes.ui.meme.mapper.toFontFamily
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorSelectionOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.theme.spacing

@Composable
fun MemeBottomBarEditorFont(
    modifier: Modifier = Modifier,
    font: MemeEditorSelectionOptions.Fonts,
    onFontClick: (MemeEvent.EditorSelectionOptionsBottomBarEvent) -> Unit
) {

    LazyRow(modifier = modifier.fillMaxWidth().wrapContentHeight()) {
        items(font.fonts) { item ->
            val background =
                if (item.isSelected) MaterialTheme.colorScheme.surfaceContainerHigh
                else Color.Transparent

            Column(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier
                        .size(width = 80.dp, height = 70.dp)
                        .background(color = background, shape = MaterialTheme.shapes.medium)
                        .clickable {
                            onFontClick(
                                MemeEvent.EditorSelectionOptionsBottomBarEvent.Font(item.id)
                            )
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = font.example,
                        modifier = Modifier
                            .weight(0.6f)
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = item.id.toFontFamily(),
                        fontSize = 24.sp,
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = item.name,
                        modifier = Modifier.weight(0.4f),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiary,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
