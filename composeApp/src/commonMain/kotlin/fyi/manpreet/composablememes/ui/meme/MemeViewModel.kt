package fyi.manpreet.composablememes.ui.meme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.repository.MemeRepository
import fyi.manpreet.composablememes.ui.meme.state.MemeEvent
import fyi.manpreet.composablememes.ui.meme.state.MemeState
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


}