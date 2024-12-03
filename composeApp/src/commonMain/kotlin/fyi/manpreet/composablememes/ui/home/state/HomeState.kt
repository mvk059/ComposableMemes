package fyi.manpreet.composablememes.ui.home.state

import fyi.manpreet.composablememes.data.model.Meme

data class HomeState(
    val memes: List<Meme> = emptyList(),
    val sortTypes: List<SortTypes> = listOf(
        SortTypes.Favorites,
        SortTypes.DateAdded,
    ),
    val selectedSortType: SortTypes = SortTypes.Favorites,
    val isSelectionMode: Boolean = false,
    val selectedItemsSize: Int = 0,
    val isDeleteDialogVisible: Boolean = false,
) {

    sealed interface SortTypes {
        data object Favorites : SortTypes
        data object DateAdded : SortTypes
    }
}
