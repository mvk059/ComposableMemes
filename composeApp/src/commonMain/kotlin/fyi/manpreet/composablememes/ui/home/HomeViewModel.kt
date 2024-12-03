package fyi.manpreet.composablememes.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.allDrawableResources
import fyi.manpreet.composablememes.data.mapper.toMemeListBottomSheet
import fyi.manpreet.composablememes.data.model.Meme
import fyi.manpreet.composablememes.data.repository.MemeRepository
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import fyi.manpreet.composablememes.ui.home.state.HomeState
import fyi.manpreet.composablememes.ui.home.state.MemeListBottomSheet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

class HomeViewModel(
    private val repository: MemeRepository,
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState
        .onStart { initHomeState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            HomeState()
        )

    private val _searchTextBottomSheet = MutableStateFlow("")
    private val searchTextBottomSheet = _searchTextBottomSheet.asStateFlow()

    private val _allMemesList = MutableStateFlow(MemeListBottomSheet())
    val allMemesList = combine(
        searchTextBottomSheet,
        _allMemesList
    ) { text, memes ->
        return@combine if (text.isEmpty()) {
            memes
        } else {
            val filteredMemes = memes.memes.filter {
                it.checkImageNameCombinations(text)
            }
            memes.copy(
                memes = filteredMemes,
                searchText = _searchTextBottomSheet.value,
            )
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _allMemesList.value
        )

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.BottomSheetEvent.OnFabClick -> onFabClick()
            is HomeEvent.BottomSheetEvent.OnSearchModeChange -> onBottomSheetSearchModeChange(event.value)
            is HomeEvent.BottomSheetEvent.OnSearchTextChange -> onBottomSheetSearchTextChange(event.text)
            is HomeEvent.BottomSheetEvent.OnMemeSelect -> onBottomSheetMemeClick(event.meme)
            is HomeEvent.MemeListEvent.OnMemeFavorite -> onMemeFavoriteClick(event.id)
            is HomeEvent.MemeListEvent.OnMemeSelect -> onMemeSelect(event.id)
            is HomeEvent.MemeListEvent.OnEnterSelectionMode -> onEnterSelectionMode(event.id)
            is HomeEvent.TopBarEvent.OnSortSelect -> onSortSelect(event.sortType)
            is HomeEvent.TopBarEvent.OnCancel -> onCancel()
            is HomeEvent.TopBarEvent.OnShare -> onShare()
            is HomeEvent.TopBarEvent.OnDeleteConfirm -> onDeleteConfirm(event.value)
            is HomeEvent.TopBarEvent.OnDelete -> onDelete()
        }
    }

    private fun initHomeState() {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedMemes = repository.getMemesSortedByFavorites()
            _homeState.update { it.copy(memes = selectedMemes) }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private fun onFabClick() {
        val allMemes = Res.allDrawableResources
            .filter { it.key.endsWith("Meme") }
            .toMemeListBottomSheet()
        _allMemesList.update { allMemes }
        resetSearchText()
    }

    private fun onBottomSheetSearchModeChange(value: Boolean) {
        _allMemesList.update { it.copy(isSearchMode = value) }
        resetSearchText()
    }

    private fun onBottomSheetSearchTextChange(text: String) {
        _searchTextBottomSheet.update { text }
    }

    private fun onBottomSheetMemeClick(meme: Meme) {
        resetSearchText()
        return
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertMeme(meme)
            updateList(sortType = _homeState.value.selectedSortType)
        }
    }

    private fun onMemeFavoriteClick(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val meme = _homeState.value.memes.first { it.id == id }
            val updatedMeme = meme.copy(isFavorite = meme.isFavorite.not())
            repository.updateMeme(updatedMeme)
            updateList(sortType = _homeState.value.selectedSortType)
        }
    }

    private fun onMemeSelect(id: Long) {
        if (!_homeState.value.isSelectionMode) return

        val memes = _homeState.value.memes.map {
            if (it.id == id) it.copy(isSelected = it.isSelected.not())
            else it
        }
        val selectedMemesSize = memes.filter { it.isSelected }.size
        val isSelectionMode = selectedMemesSize > 0
        _homeState.update {
            it.copy(
                memes = memes,
                isSelectionMode = isSelectionMode,
                selectedItemsSize = selectedMemesSize
            )
        }
    }

    private fun onSortSelect(sortType: HomeState.SortTypes) {
        _homeState.update { it.copy(selectedSortType = sortType) }
        updateList(sortType = sortType)
    }

    private fun onEnterSelectionMode(id: Long) {
        if (_homeState.value.isSelectionMode) return
        _homeState.update { it.copy(isSelectionMode = true) }
        onMemeSelect(id)
    }

    private fun onCancel() {
        val memes = _homeState.value.memes.map { it.copy(isSelected = false) }
        _homeState.update {
            it.copy(
                memes = memes,
                isSelectionMode = false,
                selectedItemsSize = 0,
                isDeleteDialogVisible = false
            )
        }
    }

    private fun onShare() {

    }

    private fun onDeleteConfirm(value: Boolean) {
        _homeState.update { it.copy(isDeleteDialogVisible = value) }
    }

    private fun onDelete() {
        onDeleteConfirm(false)
        viewModelScope.launch(Dispatchers.IO) {
            val memes = _homeState.value.memes.filter { it.isSelected }
            repository.deleteMemes(memes)
            onCancel()
            updateList(sortType = _homeState.value.selectedSortType)
        }
    }

    private fun updateList(sortType: HomeState.SortTypes) {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedMemes = when (sortType) {
                is HomeState.SortTypes.Favorites -> repository.getMemesSortedByFavorites()
                is HomeState.SortTypes.DateAdded -> repository.getMemesSortedByDate()
            }
            _homeState.update { it.copy(memes = selectedMemes) }
        }
    }

    private fun resetSearchText() {
        _searchTextBottomSheet.update { "" }
    }
}
