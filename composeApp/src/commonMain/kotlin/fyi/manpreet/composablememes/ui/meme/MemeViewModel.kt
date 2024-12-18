package fyi.manpreet.composablememes.ui.meme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.SimCardDownload
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.ic_font
import composablememes.composeapp.generated.resources.ic_font_color
import composablememes.composeapp.generated.resources.ic_font_size
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.model.MemeImageName
import fyi.manpreet.composablememes.data.model.MemeImagePath
import fyi.manpreet.composablememes.navigation.MemeDestination
import fyi.manpreet.composablememes.ui.meme.mapper.SliderValue
import fyi.manpreet.composablememes.ui.meme.mapper.sliderValueToFontSize
import fyi.manpreet.composablememes.ui.meme.state.FontFamilyType
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEditorSelectionOptions
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.MemeState
import fyi.manpreet.composablememes.ui.meme.state.MemeTextBox
import fyi.manpreet.composablememes.ui.meme.state.ShareOption
import fyi.manpreet.composablememes.ui.meme.state.resetStateWhileKeepEditorSame
import fyi.manpreet.composablememes.usecase.HistoryUseCase
import fyi.manpreet.composablememes.usecase.SaveImageUseCase
import fyi.manpreet.composablememes.util.MemeConstants
import fyi.manpreet.composablememes.util.middle
import fyi.manpreet.composablememes.util.relativeMiddle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class MemeViewModel(
    private val saveImageUseCase: SaveImageUseCase,
) : ViewModel() {

    private val _memeState = MutableStateFlow(MemeState())
    val memeState = _memeState.asStateFlow()

    private val _navigateBackStatus = MutableStateFlow(false)
    val navigateBackStatus = _navigateBackStatus.asStateFlow()

    private val historyUseCase: HistoryUseCase<MemeState> = HistoryUseCase(10)

    init {
        viewModelScope.launch {
            saveImageUseCase.saveState.collectLatest { either ->
                either.fold(
                    ifLeft = { error -> println("Error saving image: $error") },
                    ifRight = { type ->
                        when (type) {
                            ShareOption.ShareType.SAVE -> {
                                _navigateBackStatus.update { true }
                            }

                            ShareOption.ShareType.SHARE -> {}
                        }
                    }
                )
            }
            historyUseCase.recordState(_memeState.value)
        }
    }

    fun loadMeme(meme: MemeDestination) {
        _memeState.update {
            it.copy(
                meme = Meme(
                    imageName = MemeImageName(meme.memeName),
                    path = MemeImagePath(meme.memePath),
                    width = meme.width,
                    height = meme.height
                ),
                editorOptions = MemeEditorOptions(
                    editorSize = IntSize.Zero,
                    imageContentSize = Size.Zero,
                    imageContentOffset = Offset.Zero,
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
                    isUndoEnabled = false,
                    isRedoEnabled = false,
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
                shareOptions = listOf(
                    ShareOption(
                        title = "Save to device",
                        subtitle = "Save created meme in the Files of your device",
                        icon = Icons.Outlined.SimCardDownload,
                        type = ShareOption.ShareType.SAVE,
                    ),
                    ShareOption(
                        title = "Share the meme",
                        subtitle = "Share your meme or open it in the other App",
                        icon = Icons.Filled.Share,
                        type = ShareOption.ShareType.SHARE,
                    )
                ),
                shouldShowEditOptions = false
            )
        }
    }

    fun onEvent(event: MemeEvent) {
        when (event) {
            MemeEvent.TopBarEvent.ConfirmBack -> onBackConfirmTopBar()
            MemeEvent.TopBarEvent.GoBack -> onGoBack()
            MemeEvent.TopBarEvent.Cancel -> onCancelTopBar()

            MemeEvent.EditorEvent.AddTextBox -> addTextBox()
            is MemeEvent.EditorEvent.RemoveTextBox -> removeTextBox(event.id)
            is MemeEvent.EditorEvent.UpdateTextBox -> updateTextBox(event.text, event.selection)
            is MemeEvent.EditorEvent.DeselectTextBox -> deselectTextBox(
                id = event.id,
                selected = event.isSelected,
                editable = event.isEditable
            )

            MemeEvent.EditorEvent.DeselectAllTextBox -> deselectAllTextBox()
            is MemeEvent.EditorEvent.SelectTextBox -> selectTextBox(event.id)
            is MemeEvent.EditorEvent.EditTextBox -> editTextBox(event.id)
            is MemeEvent.EditorEvent.PositionUpdate -> positionUpdate(
                event.id,
                event.offset,
                event.relativePosition
            )

            is MemeEvent.EditorEvent.EditorSize -> editorSizeUpdate(
                size = event.editorSize,
                imageSize = event.imageSize,
                imageOffset = event.imageOffset
            )

            MemeEvent.EditorEvent.Undo -> undo()
            MemeEvent.EditorEvent.Redo -> redo()

            MemeEvent.EditorOptionsBottomBarEvent.Font -> onEditOptionsBottomBarFontSelection()
            MemeEvent.EditorOptionsBottomBarEvent.FontSize -> onEditOptionsBottomBarFontSizeSelection()
            MemeEvent.EditorOptionsBottomBarEvent.FontColor -> onEditOptionsBottomBarFontColorSelection()
            MemeEvent.EditorOptionsBottomBarEvent.Close -> deselectAllTextBox()
            MemeEvent.EditorOptionsBottomBarEvent.Done -> applyTextBoxStyle()

            is MemeEvent.EditorSelectionOptionsBottomBarEvent.Font -> onFontItemSelection(event.id)
            is MemeEvent.EditorSelectionOptionsBottomBarEvent.FontSize -> onFontSizeChange(event.value)
            is MemeEvent.EditorSelectionOptionsBottomBarEvent.FontColor -> onFontColorChange(event.id)

            is MemeEvent.SaveEvent.SaveImage -> saveImage(
                imageBitmap = event.imageBitmap,
                imageSize = event.size,
                imageOffset = event.offset,
                type = event.type
            )
        }
    }

    private fun updateState(newState: MemeState) {
        historyUseCase.recordState(newState)
    }

    private fun onBackConfirmTopBar() {
        _memeState.update { it.copy(isBackDialogVisible = true) }
    }

    private fun onGoBack() {
        _navigateBackStatus.update { true }
    }

    private fun onCancelTopBar() {
        _memeState.update { it.copy(isBackDialogVisible = false) }
    }

    private fun addTextBox() {
        unselectAllTextBox()
        val id = Clock.System.now().epochSeconds
        val text = "TAP TWICE TO EDIT"
        val fontSize = MemeConstants.DEFAULT_SLIDER_VALUE
        val editorSize = _memeState.value.editorOptions.editorSize
        val contentOffset = _memeState.value.editorOptions.imageContentOffset

        val newTextBox = MemeTextBox(
            id = id,
            text = text,
            textSelection = TextRange(text.length),
            offset = editorSize.middle() + contentOffset,
            relativePosition = editorSize.relativeMiddle(),
            isSelected = true,
            isEditable = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = fontSize.sliderValueToFontSize()
            ),
            fontFamilyType = FontFamilyType.AntonSC,
        )
        _memeState.update {
            it.copy(
                textBoxes = it.textBoxes + newTextBox,
                editorOptions = it.editorOptions.copy(isUndoEnabled = true),
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
        updateState(_memeState.value)
    }

    private fun updateTextBox(text: String, selection: TextRange) {
        val selectedTextBox = _memeState.value.textBoxes.find { it.isSelected } ?: return

        _memeState.update {
            it.copy(
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == selectedTextBox.id) box.copy(
                        text = text,
                        textSelection = selection
                    )
                    else box
                }
            )
        }
    }

    private fun selectTextBox(id: Long) {
        val selectedTextBox = _memeState.value.textBoxes.find { it.isSelected }
        val isEditable = selectedTextBox != null
        _memeState.update {
            it.copy(
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == id) box.copy(isSelected = true, isEditable = isEditable)
                    else box.copy(isSelected = false, isEditable = false)
                },
                shouldShowEditOptions = true
            )
        }
    }

    private fun editTextBox(id: Long) {
        _memeState.update {
            it.copy(
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == id) box.copy(isSelected = true, isEditable = true)
                    else box.copy(isSelected = false, isEditable = false)
                }
            )
        }
    }

    private fun deselectTextBox(id: Long, selected: Boolean, editable: Boolean) {
        _memeState.update {
            it.copy(
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == id) box.copy(isSelected = selected, isEditable = editable)
                    else box
                }
            )
        }
    }

    private fun deselectAllTextBox() {
        unselectAllTextBox()
        updateState(_memeState.value)
    }

    private fun applyTextBoxStyle() {
        unselectAllTextBox()
        updateState(_memeState.value)
    }

    private fun positionUpdate(
        id: Long,
        offset: Offset,
        relativePosition: MemeTextBox.RelativePosition
    ) {
        _memeState.update {
            it.copy(
                textBoxes = it.textBoxes.map { box ->
                    if (box.id == id) box.copy(offset = offset, relativePosition = relativePosition)
                    else box
                }
            )
        }
    }

    private fun editorSizeUpdate(size: IntSize, imageSize: Size, imageOffset: Offset) {
        _memeState.update {
            it.copy(
                editorOptions = it.editorOptions.copy(
                    editorSize = size,
                    imageContentSize = imageSize,
                    imageContentOffset = imageOffset
                )
            )
        }
    }

    private fun undo() {
        val state = historyUseCase.undo() ?: _memeState.value.resetStateWhileKeepEditorSame()
        _memeState.update {
            state.copy(
                editorOptions = it.editorOptions.copy(
                    isUndoEnabled = historyUseCase.canUndo(),
                    isRedoEnabled = historyUseCase.canRedo()
                ),
                shouldShowEditOptions = false,
            )
        }
    }

    private fun redo() {
        val state = historyUseCase.redo() ?: return
        _memeState.update {
            state.copy(
                editorOptions = it.editorOptions.copy(
                    isUndoEnabled = historyUseCase.canUndo(),
                    isRedoEnabled = historyUseCase.canRedo()
                ),
                shouldShowEditOptions = false,
            )
        }
    }

    private fun saveImage(
        imageBitmap: ImageBitmap,
        imageSize: Size,
        imageOffset: Offset,
        type: ShareOption.ShareType
    ) {
        viewModelScope.launch {
            saveImageUseCase.saveImage(
                imageBitmap = imageBitmap,
                imageSize = imageSize,
                imageOffset = imageOffset,
                type = type,
                meme = requireNotNull(_memeState.value.meme)
            )
        }
    }

    /**
     * Unselect all text boxes and remove text boxes with no text
     */
    private fun unselectAllTextBox() {
        // Remove text boxes with no text
        val emptyTextBox = _memeState.value.textBoxes.filter { it.text.isEmpty() }
        val allBox = _memeState.value.textBoxes.toMutableList()
        if (emptyTextBox.isNotEmpty()) {
            allBox.removeAll(emptyTextBox)
        }

        // Unselect all text boxes
        _memeState.update {
            it.copy(
                textBoxes = allBox.map { box ->
                    box.copy(isSelected = false, isEditable = false)
                },
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
