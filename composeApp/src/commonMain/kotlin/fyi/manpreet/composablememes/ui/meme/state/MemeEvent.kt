package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap

sealed interface MemeEvent {

    sealed interface TopBarEvent : MemeEvent {
        data object BackConfirm : TopBarEvent
        data object Cancel : TopBarEvent
    }

    sealed interface EditorEvent : MemeEvent {
        data object AddTextBox : EditorEvent
        data class RemoveTextBox(val id: Long) : EditorEvent
        data class UpdateTextBox(val text: String) : EditorEvent
        data class SelectTextBox(val id: Long) : EditorEvent
        data class EditTextBox(val id: Long) : EditorEvent
        data class DeselectTextBox(
            val id: Long,
            val isSelected: Boolean = false,
            val isEditable: Boolean = false
        ) : EditorEvent

        data object DeselectAllTextBox : EditorEvent
        data class PositionUpdate(val id: Long, val offset: Offset) : EditorEvent
        data class SaveImage(val imageBitmap: ImageBitmap) : EditorEvent
    }

    sealed interface EditorOptionsBottomBarEvent : MemeEvent {
        data object Font : EditorOptionsBottomBarEvent
        data object FontSize : EditorOptionsBottomBarEvent
        data object FontColor : EditorOptionsBottomBarEvent
        data object Close : EditorOptionsBottomBarEvent
        data object Done : EditorOptionsBottomBarEvent
    }

    sealed interface EditorSelectionOptionsBottomBarEvent : MemeEvent {
        data class Font(val id: FontFamilyType) : EditorSelectionOptionsBottomBarEvent
        data class FontSize(val value: Float) : EditorSelectionOptionsBottomBarEvent
        data class FontColor(val id: Long) : EditorSelectionOptionsBottomBarEvent
    }
}
