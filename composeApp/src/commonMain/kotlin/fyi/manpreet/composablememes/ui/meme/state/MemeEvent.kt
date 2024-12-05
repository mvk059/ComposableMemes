package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.geometry.Offset

sealed interface MemeEvent {

    sealed interface TopBarEvent : MemeEvent {
        data object BackConfirm : TopBarEvent
        data object Cancel : TopBarEvent
    }

    sealed interface EditorEvent : MemeEvent {
        data object AddTextBox : EditorEvent
        data class RemoveTextBox(val id: Long) : EditorEvent
        data class SelectTextBox(val id: Long) : EditorEvent
        data object DeselectTextBox : EditorEvent
        data class PositionUpdate(val id: Long, val offset: Offset) : EditorEvent
    }
}