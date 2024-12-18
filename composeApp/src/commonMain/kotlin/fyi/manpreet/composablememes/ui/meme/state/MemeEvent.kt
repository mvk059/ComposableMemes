package fyi.manpreet.composablememes.ui.meme.state

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntSize

sealed interface MemeEvent {

    sealed interface TopBarEvent : MemeEvent {
        data object ConfirmBack : TopBarEvent
        data object GoBack : TopBarEvent
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
        data class PositionUpdate(
            val id: Long,
            val offset: Offset,
            val relativePosition: MemeTextBox.RelativePosition
        ) : EditorEvent

        data class EditorSize(val editorSize: IntSize, val imageSize: Size, val imageOffset: Offset) : EditorEvent
        data object Undo : EditorEvent
        data object Redo : EditorEvent
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

    sealed interface SaveEvent : MemeEvent {
        data class SaveImage(
            val imageBitmap: ImageBitmap,
            val offset: Offset,
            val size: Size,
            val type: ShareOption.ShareType
        ) : SaveEvent
    }
}
