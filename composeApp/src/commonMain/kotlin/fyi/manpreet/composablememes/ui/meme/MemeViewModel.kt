package fyi.manpreet.composablememes.ui.meme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.repository.MemeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MemeViewModel(
    private val repository: MemeRepository,
) : ViewModel() {

    private val _meme = MutableStateFlow<Meme?>(null)
    val meme = _meme.asStateFlow()

    fun loadMeme(id: Long) {
        fetchMeme(id)
    }

    private fun fetchMeme(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val meme = repository.getMemeById(id)
            _meme.value = meme
        }
    }
}