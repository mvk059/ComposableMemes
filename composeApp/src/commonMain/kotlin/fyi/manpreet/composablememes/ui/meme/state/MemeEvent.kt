package fyi.manpreet.composablememes.ui.meme.state

sealed interface MemeEvent {

    sealed interface TopBarEvent : MemeEvent {
        data object BackConfirm : TopBarEvent
        data object Cancel : TopBarEvent
    }
}