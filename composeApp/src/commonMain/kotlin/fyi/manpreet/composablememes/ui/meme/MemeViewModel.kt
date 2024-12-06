package fyi.manpreet.composablememes.ui.meme

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.ic_font
import composablememes.composeapp.generated.resources.ic_font_color
import composablememes.composeapp.generated.resources.ic_font_size
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.repository.MemeRepository
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorSelectionOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.MemeState
import fyi.manpreet.composablememes.ui.meme.state.MemeTextBox
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

class MemeViewModel(
    private val repository: MemeRepository,
) : ViewModel() {

    private val _memeState = MutableStateFlow(MemeState())
    val memeState = _memeState.asStateFlow()

    fun loadMeme(memeName: String) {
        _memeState.update {
            it.copy(
                meme = Meme(
                    imageName = memeName,
                    createdDate = Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                ),
                editorOptions = MemeEditorOptions(
                    options = listOf(
                        MemeEditorOptions.Options(
                            type = MemeEvent.EditorOptionsBottomBarEvent.Font,
                            icon = Res.drawable.ic_font,
                            isSelected = false,
                        ),
                        MemeEditorOptions.Options(
                            type = MemeEvent.EditorOptionsBottomBarEvent.FontSize,
                            icon = Res.drawable.ic_font_size,
                            isSelected = false,
                        ),
                        MemeEditorOptions.Options(
                            type = MemeEvent.EditorOptionsBottomBarEvent.FontColor,
                            icon = Res.drawable.ic_font_color,
                            isSelected = false,
                        )
                    ),
                ),
                editorSelectionOptions = MemeEditorSelectionOptions(
                    fonts = listOf(
                        MemeEditorSelectionOptions.Font(
                            id = 1,
                            name = "Roboto",
                            type = "sans-serif",
                        ),
                        MemeEditorSelectionOptions.Font(
                            id = 2,
                            name = "Roboto Mono",
                            type = "monospace",
                        ),
                        MemeEditorSelectionOptions.Font(
                            id = 3,
                            name = "Roboto Slab",
                            type = "serif",
                        ),
                        MemeEditorSelectionOptions.Font(
                            id = 4,
                            name = "Roboto",
                            type = "sans-serif",
                        ),
                        MemeEditorSelectionOptions.Font(
                            id = 5,
                            name = "Roboto Mono",
                            type = "monospace",
                        ),
                        MemeEditorSelectionOptions.Font(
                            id = 6,
                            name = "Roboto Slab",
                            type = "serif",
                        ),
                    )
                ),
                shouldShowEditOptions = false
            )
        }
    }

    fun onEvent(event: MemeEvent) {
        when (event) {
            MemeEvent.TopBarEvent.BackConfirm -> onBackConfirmTopBar()
            MemeEvent.TopBarEvent.Cancel -> onCancelTopBar()

            MemeEvent.EditorEvent.AddTextBox -> addTextBox()
            is MemeEvent.EditorEvent.RemoveTextBox -> removeTextBox(event.id)
            is MemeEvent.EditorEvent.DeselectTextBox -> deselectTextBox()
            is MemeEvent.EditorEvent.SelectTextBox -> selectTextBox(event.id)
            is MemeEvent.EditorEvent.PositionUpdate -> positionUpdate(event.id, event.offset)

            is MemeEvent.EditorOptionsBottomBarEvent.Font -> onEditOptionsBottomBarFontSelection()
            is MemeEvent.EditorOptionsBottomBarEvent.FontSize -> {}
            is MemeEvent.EditorOptionsBottomBarEvent.FontColor -> {}
            MemeEvent.EditorOptionsBottomBarEvent.Close -> deselectTextBox()
            MemeEvent.EditorOptionsBottomBarEvent.Done -> {}

            is MemeEvent.EditorSelectionOptionsBottomBarEvent.Font -> onFontItemSelection(event.id)
            is MemeEvent.EditorSelectionOptionsBottomBarEvent.FontSize -> {}
            is MemeEvent.EditorSelectionOptionsBottomBarEvent.FontColor -> {}
        }
    }

    private fun fetchMeme(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val meme = repository.getMemeById(id)
            _memeState.update { it.copy(meme = meme) }
        }
    }

    private fun onBackConfirmTopBar() {
        _memeState.update { it.copy(isBackDialogVisible = true) }
    }

    private fun onCancelTopBar() {
        _memeState.update { it.copy(isBackDialogVisible = false) }
    }

    private fun addTextBox() {
        unselectAllTextBox()
        val text = "TAP TWICE TO EDIT ${Random.nextInt(0, 10)}"
        val newTextBox = MemeTextBox(
            id = Clock.System.now().epochSeconds,
            text = text,
            offset = Offset.Zero,
            isSelected = true,
        )
        _memeState.update {
            it.copy(
                textBoxes = it.textBoxes + newTextBox,
                shouldShowEditOptions = true,
            )
        }
        onEditOptionsBottomBarFontSelection()
    }

    private fun removeTextBox(id: Long) {
        _memeState.update { currentState ->
            currentState.copy(
                textBoxes = currentState.textBoxes.filter { it.id != id },
                shouldShowEditOptions = false
            )
        }
    }

    private fun selectTextBox(id: Long) {
        _memeState.update {
            it.copy(
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == id) box.copy(isSelected = true)
                    else box.copy(isSelected = false)
                },
                shouldShowEditOptions = true
            )
        }
    }

    private fun deselectTextBox() {
        unselectAllTextBox()
    }

    private fun positionUpdate(id: Long, offset: Offset) {
        _memeState.update {
            it.copy(textBoxes = it.textBoxes.map { box ->
                if (box.id == id) box.copy(offset = offset)
                else box
            })
        }
    }

    private fun unselectAllTextBox() {
        _memeState.update {
            it.copy(
                textBoxes = it.textBoxes.map { box -> box.copy(isSelected = false) },
                shouldShowEditOptions = false
            )
        }
    }

    private fun onEditOptionsBottomBarFontSelection() {
        updateEditOptionsBottomBarSelection(MemeEvent.EditorOptionsBottomBarEvent.Font)
        onFontItemSelection(_memeState.value.editorSelectionOptions.fonts.first().id)
    }

    private fun updateEditOptionsBottomBarSelection(type: MemeEvent.EditorOptionsBottomBarEvent) {
        _memeState.update {
            it.copy(
                editorOptions = it.editorOptions.copy(
                    options = it.editorOptions.options.map { option ->
                        if (option.type == type) {
                            option.copy(isSelected = true)
                        } else {
                            option.copy(isSelected = false)
                        }
                    }
                )
            )
        }
    }

    private fun onFontItemSelection(id: Long) {
//        val selectedTextBox = _memeState.value.textBoxes.find { it.isSelected } ?: return

        _memeState.update {
            it.copy(
                editorSelectionOptions = it.editorSelectionOptions.copy(
                    fonts = it.editorSelectionOptions.fonts.map { font ->
                        if (font.id == id) font.copy(isSelected = true)
                        else font.copy(isSelected = false)
                    }
                )
            )
        }

//        _memeState.update {
//            it.copy(
//                textBoxes = it.textBoxes.map { box ->
//                    if (box.id == selectedTextBox.id) {
//                        box.copy(font = it.editorSelectionOptions.fonts.find { it.id == id }!!)
//                    } else box
//                }
//            )
//        }
    }
}
