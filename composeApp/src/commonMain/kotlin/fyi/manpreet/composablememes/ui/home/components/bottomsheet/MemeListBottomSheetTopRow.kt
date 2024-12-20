package fyi.manpreet.composablememes.ui.home.components.bottomsheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.composables.core.Icon
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.home_bottom_sheet_choose_template_subtitle
import composablememes.composeapp.generated.resources.home_bottom_sheet_choose_template_title
import composablememes.composeapp.generated.resources.home_bottom_sheet_input_list_size
import composablememes.composeapp.generated.resources.home_bottom_sheet_input_no_memes_found
import composablememes.composeapp.generated.resources.meme_search_cd
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import fyi.manpreet.composablememes.ui.theme.fixedAccentColors
import fyi.manpreet.composablememes.ui.theme.spacing
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MemeListBottomSheetTopBar(
    modifier: Modifier = Modifier,
    searchMode: Boolean,
    toggleSearchMode: (HomeEvent.BottomSheetEvent) -> Unit,
    searchText: String,
    onSearchTextChange: (HomeEvent.BottomSheetEvent) -> Unit,
    inputPlaceHolder: StringResource,
    memesListSize: Int,
) {

    if (!searchMode) {
        TextContent(
            modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
            toggleSearchMode = toggleSearchMode,
        )
    } else {
        SearchContent(
            modifier = modifier,
            toggleSearchMode = toggleSearchMode,
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            inputPlaceHolder = inputPlaceHolder,
            memesListSize = memesListSize,
        )
    }
}

@Composable
private fun TextContent(
    modifier: Modifier = Modifier,
    toggleSearchMode: (HomeEvent.BottomSheetEvent) -> Unit,
) {
    Column {

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = stringResource(Res.string.home_bottom_sheet_choose_template_title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Icon(
                modifier = Modifier.clickable {
                    toggleSearchMode(
                        HomeEvent.BottomSheetEvent.OnSearchModeChange(true)
                    )
                },
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
}

@Composable
private fun SearchContent(
    modifier: Modifier = Modifier,
    toggleSearchMode: (HomeEvent.BottomSheetEvent) -> Unit,
    searchText: String,
    onSearchTextChange: (HomeEvent.BottomSheetEvent) -> Unit,
    inputPlaceHolder: StringResource,
    memesListSize: Int,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Column {

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = searchText,
                onValueChange = {
                    onSearchTextChange(
                        HomeEvent.BottomSheetEvent.OnSearchTextChange(
                            it
                        )
                    )
                },
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                ),
                placeholder = {
                    Text(
                        text = stringResource(inputPlaceHolder),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.outline
                        ),
                    )
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.fixedAccentColors.secondaryFixedDim,
                        modifier = Modifier.clickable {
                            toggleSearchMode(
                                HomeEvent.BottomSheetEvent.OnSearchModeChange(false)
                            )
                        }
                    )
                },
                trailingIcon = {
                    if (searchText.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = MaterialTheme.fixedAccentColors.secondaryFixedDim,
                            modifier = Modifier.clickable {
                                onSearchTextChange(
                                    HomeEvent.BottomSheetEvent.OnSearchTextChange("")
                                )
                            }
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                )
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth().padding(top = MaterialTheme.spacing.medium),
            text =
            if (memesListSize == 0) stringResource(Res.string.home_bottom_sheet_input_no_memes_found)
            else stringResource(Res.string.home_bottom_sheet_input_list_size, memesListSize),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}
