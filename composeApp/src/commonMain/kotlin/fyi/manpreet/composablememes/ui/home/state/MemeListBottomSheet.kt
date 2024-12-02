package fyi.manpreet.composablememes.ui.home.state

import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.home_bottom_sheet_input_placeholder
import fyi.manpreet.composablememes.data.model.Meme
import org.jetbrains.compose.resources.StringResource

data class MemeListBottomSheet(
    val memes: List<Meme> = emptyList(),
    val searchText: String = "",
    val isSearchMode: Boolean = false,
    val placeholder: StringResource = Res.string.home_bottom_sheet_input_placeholder,
)
