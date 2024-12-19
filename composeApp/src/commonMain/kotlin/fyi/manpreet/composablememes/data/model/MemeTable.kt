package fyi.manpreet.composablememes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MemeTable(
    val memes: List<MemeData> = emptyList(),
) {

    @Serializable
    data class MemeData(
        val id: Long = 0L,
        val imageUrl: String,
        val createdDateInMillis: Long,
        val path: String,
        val isFavorite: Boolean,
    )
}
