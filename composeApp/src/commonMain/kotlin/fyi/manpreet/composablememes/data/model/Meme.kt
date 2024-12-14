package fyi.manpreet.composablememes.data.model

import kotlin.jvm.JvmInline

data class Meme(
    val id: Long = INVALID_ID,
    val imageName: MemeImageName,
    val path: MemeImagePath? = null,
    val isFavorite: Boolean = false,
    val isSelected: Boolean = false,
) {

    fun checkImageNameCombinations(searchValue: String): Boolean {
        val name = imageName.value.substring(0, imageName.value.lastIndexOf("_"))
        return name.contains(searchValue, ignoreCase = true)
    }

    companion object {
        const val INVALID_ID = 0L
    }
}

@JvmInline
value class MemeImageName(val value: String)

@JvmInline
value class MemeImagePath(val value: String)