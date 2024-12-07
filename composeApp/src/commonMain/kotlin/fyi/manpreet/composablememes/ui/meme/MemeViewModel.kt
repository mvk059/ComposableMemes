package fyi.manpreet.composablememes.ui.meme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.ic_font
import composablememes.composeapp.generated.resources.ic_font_color
import composablememes.composeapp.generated.resources.ic_font_size
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.repository.MemeRepository
import fyi.manpreet.composablememes.ui.meme.mapper.SliderValue
import fyi.manpreet.composablememes.ui.meme.mapper.sliderValueToFontSize
import fyi.manpreet.composablememes.ui.meme.state.FontFamilyType
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorSelectionOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.MemeState
import fyi.manpreet.composablememes.ui.meme.state.MemeTextBox
import fyi.manpreet.composablememes.util.MemeConstants
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
                        ),
                        MemeEditorOptions.Options(
                            type = MemeEvent.EditorOptionsBottomBarEvent.FontSize,
                            icon = Res.drawable.ic_font_size,
                        ),
                        MemeEditorOptions.Options(
                            type = MemeEvent.EditorOptionsBottomBarEvent.FontColor,
                            icon = Res.drawable.ic_font_color,
                        )
                    ),
                    selectedOption = MemeEvent.EditorOptionsBottomBarEvent.Font
                ),
                editorSelectionOptions = MemeEditorSelectionOptions(
                    font = MemeEditorSelectionOptions.Fonts(
                        example = "Good",
                        fonts = listOf(
                            MemeEditorSelectionOptions.Fonts.Font(
                                id = FontFamilyType.AntonSC,
                                name = "Anton SC",
                            ),
                            MemeEditorSelectionOptions.Fonts.Font(
                                id = FontFamilyType.DancingScript,
                                name = "Dancing Script",
                            ),
                            MemeEditorSelectionOptions.Fonts.Font(
                                id = FontFamilyType.Jaro,
                                name = "Jaro",
                            ),
                            MemeEditorSelectionOptions.Fonts.Font(
                                id = FontFamilyType.Lobster,
                                name = "Lobster",
                            ),
                            MemeEditorSelectionOptions.Fonts.Font(
                                id = FontFamilyType.Manrope,
                                name = "Manrope",
                            ),
//                            MemeEditorSelectionOptions.Fonts.Font(
//                                id = FontFamilyType.NotoSansSymbols,
//                                name = "Noto Sans Symbols",
//                            ),
                            MemeEditorSelectionOptions.Fonts.Font(
                                id = FontFamilyType.OpenSans,
                                name = "Open Sans",
                            ),
                            MemeEditorSelectionOptions.Fonts.Font(
                                id = FontFamilyType.Roboto,
                                name = "Roboto",
                            ),
                            MemeEditorSelectionOptions.Fonts.Font(
                                id = FontFamilyType.RubrikDoodleShadow,
                                name = "Rubrik Doodle Shadow",
                            ),
                        ),
                    ),
                    fontSize = MemeConstants.DEFAULT_SLIDER_VALUE,
                    fontColors = listOf(
                        MemeEditorSelectionOptions.FontColor(
                            id = 1,
                            color = Color.White,
                        ),
                        MemeEditorSelectionOptions.FontColor(
                            id = 2,
                            color = Color.Yellow,
                        ),
                        MemeEditorSelectionOptions.FontColor(
                            id = 3,
                            color = Color.Red,
                        ),
                        MemeEditorSelectionOptions.FontColor(
                            id = 4,
                            color = Color.Magenta,
                        ),
                        MemeEditorSelectionOptions.FontColor(
                            id = 5,
                            color = Color.Cyan,
                        ),
                        MemeEditorSelectionOptions.FontColor(
                            id = 6,
                            color = Color.Green,
                        ),
                        MemeEditorSelectionOptions.FontColor(
                            id = 7,
                            color = Color.Black,
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
            is MemeEvent.EditorOptionsBottomBarEvent.FontSize -> onEditOptionsBottomBarFontSizeSelection()
            is MemeEvent.EditorOptionsBottomBarEvent.FontColor -> onEditOptionsBottomBarFontColorSelection()
            MemeEvent.EditorOptionsBottomBarEvent.Close -> removeTextBox()
            MemeEvent.EditorOptionsBottomBarEvent.Done -> applyTextBoxStyle()

            is MemeEvent.EditorSelectionOptionsBottomBarEvent.Font -> onFontItemSelection(event.id)
            is MemeEvent.EditorSelectionOptionsBottomBarEvent.FontSize -> onFontSizeChange(event.value)
            is MemeEvent.EditorSelectionOptionsBottomBarEvent.FontColor -> onFontColorChange(event.id)
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
        val id = Clock.System.now().epochSeconds
        val text = "TAP TWICE TO EDIT ${Random.nextInt(0, 10)}"
        val fontSize = MemeConstants.DEFAULT_SLIDER_VALUE
        val newTextBox = MemeTextBox(
            id = id,
            text = text,
            offset = Offset.Zero,
            isSelected = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = fontSize.sliderValueToFontSize()
            ),
            fontFamilyType = FontFamilyType.AntonSC,
        )
        _memeState.update {
            it.copy(
                textBoxes = it.textBoxes + newTextBox,
                editorSelectionOptions = it.editorSelectionOptions.copy(fontSize = fontSize),
                shouldShowEditOptions = true,
            )
        }
        onEditOptionsBottomBarFontSelection()
    }

    private fun removeTextBox(id: Long? = null) {
        val box = if (id != null) _memeState.value.textBoxes.find { it.id == id }
        else _memeState.value.textBoxes.find { it.isSelected }

        if (box == null) return
        val allBox = _memeState.value.textBoxes.toMutableList()
        allBox.remove(box)

        _memeState.update { currentState ->
            currentState.copy(
                textBoxes = allBox,
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

    private fun applyTextBoxStyle() {
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
        onFontItemSelection(_memeState.value.editorSelectionOptions.font.fonts.first().id)
    }

    private fun onEditOptionsBottomBarFontSizeSelection() {
        updateEditOptionsBottomBarSelection(MemeEvent.EditorOptionsBottomBarEvent.FontSize)
    }

    private fun onEditOptionsBottomBarFontColorSelection() {
        updateEditOptionsBottomBarSelection(MemeEvent.EditorOptionsBottomBarEvent.FontColor)
        onFontColorChange(_memeState.value.editorSelectionOptions.fontColors.first().id)
    }

    private fun updateEditOptionsBottomBarSelection(type: MemeEvent.EditorOptionsBottomBarEvent) {
        _memeState.update { it.copy(editorOptions = it.editorOptions.copy(selectedOption = type)) }
    }

    private fun onFontItemSelection(id: FontFamilyType) {
        val selectedTextBox = _memeState.value.textBoxes.find { it.isSelected } ?: return

        _memeState.update {
            it.copy(
                editorSelectionOptions = it.editorSelectionOptions.copy(
                    font = it.editorSelectionOptions.font.copy(
                        fonts = it.editorSelectionOptions.font.fonts.map { font ->
                            if (font.id == id) font.copy(isSelected = true)
                            else font.copy(isSelected = false)
                        }
                    )
                ),
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == selectedTextBox.id) box.copy(fontFamilyType = id)
                    else box
                }
            )
        }
    }

    private fun onFontSizeChange(value: SliderValue) {
        val selectedTextBox = _memeState.value.textBoxes.find { it.isSelected } ?: return

        _memeState.update {
            it.copy(
                editorSelectionOptions = it.editorSelectionOptions.copy(
                    fontSize = value
                ),
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == selectedTextBox.id)
                        box.copy(
                            textStyle = selectedTextBox.textStyle.copy(
                                fontSize = value.sliderValueToFontSize()
                            )
                        )
                    else box
                }
            )
        }

    }

    private fun onFontColorChange(id: Long) {
        val selectedTextBox = _memeState.value.textBoxes.find { it.isSelected } ?: return
        val selectedColor =
            _memeState.value.editorSelectionOptions.fontColors.find { it.id == id }?.color ?: return

        _memeState.update {
            it.copy(
                editorSelectionOptions = it.editorSelectionOptions.copy(
                    fontColors = it.editorSelectionOptions.fontColors.map { color ->
                        if (color.id == id) color.copy(isSelected = true)
                        else color.copy(isSelected = false)
                    },
                ),
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == selectedTextBox.id) box.copy(textStyle = box.textStyle.copy(color = selectedColor))
                    else box
                }
            )
        }
    }
}
