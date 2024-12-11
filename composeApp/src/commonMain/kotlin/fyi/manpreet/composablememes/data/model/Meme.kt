package fyi.manpreet.composablememes.data.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Meme(
    val id: Long = INVALID_ID,
    val imageName: String,
    val createdDate: LocalDateTime,
    val path: String? = null,
    val isFavorite: Boolean = false,
    val isSelected: Boolean = false,
) {

    fun checkImageNameCombinations(searchValue: String): Boolean {
        return imageName.contains(searchValue, ignoreCase = true)
    }

    companion object {
        const val INVALID_ID = 0L
    }
}
