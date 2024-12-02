package fyi.manpreet.composablememes.ui.home.components.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.Icon
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Sheet
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.home_bottom_sheet_choose_template_subtitle
import composablememes.composeapp.generated.resources.home_bottom_sheet_choose_template_title
import composablememes.composeapp.generated.resources.meme_search_cd
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.ui.home.components.item.MemeItem
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@Composable
fun MemeListBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState,
    memes: List<Meme>,
) {

    ModalBottomSheet(
        state = sheetState,
    ) {
        Sheet(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1200.dp)
                    .padding(
                        WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)
                            .asPaddingValues()
                    )
                    .navigationBarsPadding(),
                contentAlignment = Alignment.TopCenter
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.small),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DragIndication(
                        modifier = Modifier
                            .padding(top = MaterialTheme.spacing.large)
                            .background(color = MaterialTheme.colorScheme.outline)
                            .width(MaterialTheme.spacing.largeXL)
                            .height(MaterialTheme.spacing.extraSmall)
                    )

                    TopRow()
                    ListContent(memes = memes)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.TopRow(
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = stringResource(Res.string.home_bottom_sheet_choose_template_title),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(Res.string.meme_search_cd),
            tint = MaterialTheme.fixedAccentColors.secondaryFixedDim,
        )
    }

    Text(
        modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.small),
        text = stringResource(Res.string.home_bottom_sheet_choose_template_subtitle),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onTertiary,
    )
}

@Composable
private fun ColumnScope.ListContent(
    modifier: Modifier = Modifier,
    memes: List<Meme>,
) {

    LazyVerticalGrid(
        modifier = modifier.padding(top = MaterialTheme.spacing.large2XL),
        columns = GridCells.Fixed(2),
    ) {

        items(
            items = memes,
            key = { memes -> memes.imageUrl },
        ) { meme ->

            MemeItem(
                meme = meme,
            )
        }

    }
}