package fyi.manpreet.composablememes.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.raise.either
import composablememes.composeapp.generated.resources.Res
import composablememes.composeapp.generated.resources.allDrawableResources
import fyi.manpreet.composablememes.data.mapper.toMemeListBottomSheet
import fyi.manpreet.composablememes.data.repository.MemeRepository
import fyi.manpreet.composablememes.platform.filemanager.FileManager
import fyi.manpreet.composablememes.ui.home.state.HomeEvent
import fyi.manpreet.composablememes.ui.home.state.HomeState
import fyi.manpreet.composablememes.ui.home.state.MemeListBottomSheet
import fyi.manpreet.composablememes.usecase.SaveImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

class HomeViewModel(
    private val repository: MemeRepository,
    private val fileManager: FileManager,
    private val saveImageUseCase: SaveImageUseCase,
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
            is HomeEvent.BottomSheetEvent.OnMemeSelect -> onBottomSheetMemeClick()
            is HomeEvent.MemeListEvent.OnMemeFavorite -> onMemeFavoriteClick(event.id)
            is HomeEvent.MemeListEvent.OnMemeSelect -> onMemeSelect(event.id)
            is HomeEvent.MemeListEvent.OnEnterSelectionMode -> onEnterSelectionMode(event.id)
            is HomeEvent.TopBarEvent.OnSortSelect -> onSortSelect(event.sortType)
            is HomeEvent.TopBarEvent.OnCancel -> onCancel()
            is HomeEvent.TopBarEvent.OnShare -> onShare()
            is HomeEvent.TopBarEvent.OnDeleteConfirm -> onDeleteConfirm(event.value)
            is HomeEvent.TopBarEvent.OnDelete -> onDelete()
            HomeEvent.OnReload -> initHomeState()
        }
    }

    private fun initHomeState() {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedMemes = repository.getMemesSortedByFavorites()
            _homeState.update { it.copy(memes = selectedMemes) }
            deleteTemporaryImages()
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    private fun onFabClick() {
        onCancel()
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

    private fun onBottomSheetMemeClick() {
        resetSearchText()
        return
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
        viewModelScope.launch(Dispatchers.IO) {
            val memes = _homeState.value.memes.filter { it.isSelected }.map { it.imageName }
            saveImageUseCase.saveImages(memes)
        }
    }

    private fun onDeleteConfirm(value: Boolean) {
        _homeState.update { it.copy(isDeleteDialogVisible = value) }
    }

    private fun onDelete() {
        onDeleteConfirm(false)
        deleteFiles()
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

    private fun deleteFiles() {
        viewModelScope.launch(Dispatchers.IO) {
            val memes = _homeState.value.memes.filter { it.isSelected }
            either {
                with(fileManager) {
                    memes.forEach { meme ->
                        this@either.deleteImage(meme.imageName)
                    }
                }
            }.fold(
                ifLeft = { println("Failed to delete image: $it") },
                ifRight = {
                    repository.deleteMemes(memes)
                    onCancel()
                    updateList(sortType = _homeState.value.selectedSortType)
                }
            )
        }
    }

    private fun deleteTemporaryImages() {
        viewModelScope.launch {
            either {
                with(fileManager) {
                    this@either.deleteTemporaryImage()
                }
            }.fold(
                ifLeft = { println("Error deleting temporary images: $it") },
                ifRight = { println("Temporary images deleted successfully") }
            )
        }
    }
}
