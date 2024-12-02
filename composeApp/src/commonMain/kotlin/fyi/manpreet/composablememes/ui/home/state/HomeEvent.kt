package fyi.manpreet.composablememes.ui.home.state

sealed interface HomeEvent {

    sealed interface BottomSheetEvent : HomeEvent {
        data object OnFabClick : BottomSheetEvent
        data class OnSearchTextChange(val text: String) : BottomSheetEvent
        data class OnSearchModeChange(val value: Boolean) : BottomSheetEvent
    }

}
