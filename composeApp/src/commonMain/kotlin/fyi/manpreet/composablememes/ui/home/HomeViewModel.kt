package fyi.manpreet.composablememes.ui.home

import androidx.lifecycle.ViewModel
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.allDrawableResources
import fyi.manpreet.composablememes.data.mapper.toMeme
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.repository.MemeRepository
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.ExperimentalResourceApi

class HomeViewModel(
    private val repository: MemeRepository,
) : ViewModel() {

    private val _memeList = MutableStateFlow(emptyList<Meme>())
    val memeList = _memeList.asStateFlow()

    private val _allMemesList = MutableStateFlow(emptyList<Meme>())
    val allMemesList = _allMemesList.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnFabClick -> onFabClick()
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private fun onFabClick() {
        val allMemes = Res.allDrawableResources.filter { it.key.endsWith("Meme") }.toMeme()
        _allMemesList.update { allMemes }
    }
}
