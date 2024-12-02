package fyi.manpreet.composablememes.ui.home

import androidx.lifecycle.ViewModel
import fyi.manpreet.composablememes.data.repository.MemeRepository

class HomeViewModel(
    private val repository: MemeRepository
): ViewModel() {

    fun getAllMemes() = repository.getMemesSortedByDate()
}