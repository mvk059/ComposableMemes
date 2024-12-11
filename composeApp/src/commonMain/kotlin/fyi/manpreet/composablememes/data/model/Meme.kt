package fyi.manpreet.composablememes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Meme(
    val id: Long = INVALID_ID,
    val imageName: String,
    val path: String? = null,
    val isFavorite: Boolean = false,
    val isSelected: Boolean = false,
) {

    fun checkImageNameCombinations(searchValue: String): Boolean {
        val name = imageName.substring(0, imageName.lastIndexOf("_"))
        return name.contains(searchValue, ignoreCase = true)
    }

    companion object {
        const val INVALID_ID = 0L
    }
}
