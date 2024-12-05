package fyi.manpreet.composablememes.ui.home.components.topbar

import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.composables.core.Icon
import com.composables.core.Menu
import com.composables.core.MenuButton
import com.composables.core.MenuContent
import com.composables.core.MenuItem
import com.composables.core.rememberMenuState
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.home_appbar_title
import composablememes.composeapp.generated.resources.home_sort_favorite_first
import composablememes.composeapp.generated.resources.home_sort_newest_first
import composablememes.composeapp.generated.resources.meme_sort_cd
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import fyi.manpreet.composablememes.ui.home.state.HomeState
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    selectedItems: Int,
    isSelectionMode: Boolean,
    sortTypes: List<HomeState.SortTypes>,
    selectedSortType: HomeState.SortTypes,
    onSelectedSortType: (HomeEvent.TopBarEvent) -> Unit,
    onCancelClick: (HomeEvent.TopBarEvent) -> Unit,
    onShareClick: (HomeEvent.TopBarEvent) -> Unit,
    onDeleteClick: (HomeEvent.TopBarEvent) -> Unit,
) {

    TopAppBar(
        modifier = modifier.background(color = MaterialTheme.colorScheme.surfaceContainerLow),
        title = {
            Text(stringResource(Res.string.home_appbar_title))
        },
        actions = {
            if (isSelectionMode) {
                SelectionMode(
                    selectedItems = selectedItems,
                    onCancelClick = onCancelClick,
                    onShareClick = onShareClick,
                    onDeleteClick = onDeleteClick,
                )
            } else {
                SortDropdown(
                    sortItems = sortTypes,
                    selectedSortType = selectedSortType,
                    onSelectedSortType = onSelectedSortType,
                )

            }
        }
    )
}

@Composable
private fun RowScope.SelectionMode(
    selectedItems: Int,
    onCancelClick: (HomeEvent.TopBarEvent) -> Unit,
    onShareClick: (HomeEvent.TopBarEvent) -> Unit,
    onDeleteClick: (HomeEvent.TopBarEvent) -> Unit,
) {

    Icon(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.medium)
            .clickable { onCancelClick(HomeEvent.TopBarEvent.OnCancel) },
        imageVector = Icons.Default.Close,
        contentDescription = null,
        tint = MaterialTheme.fixedAccentColors.secondaryFixedDim,
    )

    Text(
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
        text = selectedItems.toString(),
        style = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    )

    Spacer(Modifier.weight(1f))

    Icon(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.medium)
            .clickable { onShareClick(HomeEvent.TopBarEvent.OnShare) },
        imageVector = Icons.Default.Share,
        contentDescription = null,
        tint = MaterialTheme.fixedAccentColors.secondaryFixedDim,
    )

    Icon(
        modifier = Modifier
            .padding(horizontal = MaterialTheme.spacing.medium)
            .clickable { onDeleteClick(HomeEvent.TopBarEvent.OnDeleteConfirm(true)) },
        imageVector = Icons.Outlined.Delete,
        contentDescription = null,
        tint = MaterialTheme.fixedAccentColors.secondaryFixedDim,
    )

}

@Composable
private fun RowScope.SortDropdown(
    modifier: Modifier = Modifier,
    sortItems: List<HomeState.SortTypes>,
    selectedSortType: HomeState.SortTypes,
    onSelectedSortType: (HomeEvent.TopBarEvent) -> Unit,
) {
    val state = rememberMenuState(expanded = false)

    Column(
        modifier = modifier.wrapContentSize().align(Alignment.CenterVertically),
    ) {
        Menu(state, modifier = Modifier.align(Alignment.End)) {
            MenuButton {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    val text = when (selectedSortType) {
                        is HomeState.SortTypes.Favorites -> stringResource(Res.string.home_sort_favorite_first)
                        is HomeState.SortTypes.DateAdded -> stringResource(Res.string.home_sort_newest_first)
                    }
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(Modifier.width(MaterialTheme.spacing.extraSmall))

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = stringResource(Res.string.meme_sort_cd),
                        tint = MaterialTheme.fixedAccentColors.secondaryFixedDim,
                    )
                }
            }

            MenuContent(
                modifier = Modifier
                    .width(MaterialTheme.spacing.sortFilterSize)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .padding(MaterialTheme.spacing.extraSmall),
                exit = fadeOut()
            ) {
                sortItems.forEach { item ->

                    MenuItem(
                        modifier = Modifier.clip(RoundedCornerShape(MaterialTheme.spacing.extraSmall)),
                        onClick = { onSelectedSortType(HomeEvent.TopBarEvent.OnSortSelect(item)) }
                    ) {
                        val text = when (item) {
                            is HomeState.SortTypes.Favorites -> stringResource(Res.string.home_sort_favorite_first)
                            is HomeState.SortTypes.DateAdded -> stringResource(Res.string.home_sort_newest_first)
                        }

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    vertical = MaterialTheme.spacing.small,
                                    horizontal = MaterialTheme.spacing.extraSmall
                                ),
                            text = text,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.fixedAccentColors.secondaryFixedDim
                            ),
                        )
                    }
                }
            }
        }
    }
}