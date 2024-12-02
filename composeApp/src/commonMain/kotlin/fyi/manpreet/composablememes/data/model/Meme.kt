package fyi.manpreet.composablememes.data.model

import kotlinx.datetime.LocalDateTime

data class Meme(
    val id: Long = INVALID_ID,
    val imageUrl: String,
    val createdDate: LocalDateTime,
    val isFavorite: Boolean,
) {

    fun checkImageNameCombinations(searchValue: String): Boolean {
        return imageUrl.contains(searchValue, ignoreCase = true)
    }

    companion object {
        const val INVALID_ID = 0L
    }
}
