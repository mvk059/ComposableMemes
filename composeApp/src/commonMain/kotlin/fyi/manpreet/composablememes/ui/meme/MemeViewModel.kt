package fyi.manpreet.composablememes.ui.meme

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.repository.MemeRepository
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
                )
            )
        }
    }

    fun onEvent(event: MemeEvent) {
        when (event) {
            MemeEvent.TopBarEvent.BackConfirm -> onBackConfirmTopBar()
            MemeEvent.TopBarEvent.Cancel -> onCancelTopBar()
            MemeEvent.EditorEvent.AddTextBox -> addTextBox()
            is MemeEvent.EditorEvent.RemoveTextBox -> removeTextBox(event.id)
            MemeEvent.EditorEvent.DeselectTextBox -> deselectTextBox()
            is MemeEvent.EditorEvent.SelectTextBox -> selectTextBox(event.id)
            is MemeEvent.EditorEvent.PositionUpdate -> positionUpdate(event.id, event.offset)
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
        val text = Clock.System.now().epochSeconds
        val newTextBox = MemeTextBox(
            id = text,
            text = text.toString(),
            offset = Offset.Zero,
            isSelected = true,
        )
        _memeState.update {
            it.copy(textBoxes = it.textBoxes + newTextBox)
        }
    }

    private fun removeTextBox(id: Long) {
        _memeState.update { currentState ->
            currentState.copy(
                textBoxes = currentState.textBoxes.filter { it.id != id }
            )
        }
    }

    private fun selectTextBox(id: Long) {
        _memeState.update {
            it.copy(textBoxes = it.textBoxes.map { box ->
                if (box.id == id) box.copy(isSelected = true)
                else box.copy(isSelected = false)
            })
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
            it.copy(textBoxes = it.textBoxes.map { it.copy(isSelected = false) })
        }
    }
}
