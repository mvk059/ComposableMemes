package fyi.manpreet.composablememes.data.model

import kotlin.jvm.JvmInline

data class Meme(
    val id: Long = INVALID_ID,
    val imageName: MemeImageName,
    val path: MemeImagePath? = null,
    val createdDateInMillis: Long ,
    val isFavorite: Boolean = false,
    val isSelected: Boolean = false,
    val width: Int = 0,
    val height: Int = 0,
) {

    fun checkImageNameCombinations(searchValue: String): Boolean {
        return imageName.value.contains(searchValue, ignoreCase = true)
    }

    companion object {
        const val INVALID_ID = 0L
    }
}

@JvmInline
value class MemeImageName(val value: String)

@JvmInline
value class MemeImagePath(val value: String)