package fyi.manpreet.composablememes.ui.home.state

import fyi.manpreet.composablememes.data.model.Meme

sealed interface HomeEvent {

    sealed interface BottomSheetEvent : HomeEvent {
        data object OnFabClick : BottomSheetEvent
        data class OnSearchTextChange(val text: String) : BottomSheetEvent
        data class OnSearchModeChange(val value: Boolean) : BottomSheetEvent
        data class OnMemeSelect(val meme: Meme) : BottomSheetEvent
    }

    sealed interface MemeListEvent : HomeEvent {
        data class OnMemeFavorite(val id: Long) : MemeListEvent
        data class OnSortSelect(val sortType: HomeState.SortTypes) : MemeListEvent
    }

}
